import os
import pandas as pd
from matplotlib import pyplot as plt
from plot_utils import configure_plot_presets
from plot_utils import plot_cientific_notation
from plot_utils import plot_set_right_legends
def plot_all_msds(directory,output_csv_file):
    all_data = []  # Lista para almacenar los datos de cada archivo
    configure_plot_presets(plt)

    curve_number = 1

    for filename in os.listdir(directory):
        if filename.endswith('.csv'):
            csv_path = os.path.join(directory, filename)
            data = pd.read_csv(csv_path)

            if 'Real Time' not in data.columns or 'MSD' not in data.columns:
                continue  # Saltar archivos que no tengan las columnas necesarias

            data.sort_values('Real Time', inplace=True)
            all_data.append(data)
            plt.plot(data['Real Time'], data['MSD'], '--', label=f'DC {curve_number}', linewidth=1)

            curve_number += 1

    if all_data:
        # Concatenar todos los DataFrames y calcular el promedio
        all_data_df = pd.concat(all_data).groupby('Real Time').mean().reset_index()
        print(all_data_df)
        plt.plot(all_data_df['Real Time'], all_data_df['MSD'], 'k-', label=f'DCM', linewidth=2)

    mean_msd_df = pd.DataFrame({
        'Time': all_data_df['Real Time'],
        'Mean_MSD': all_data_df['MSD']
    })
    mean_msd_df.to_csv(output_csv_file, index=False)

    # Configurar el gráfico
    plt.xlabel('Tiempo (s)')
    plt.ylabel('DCM (m$^2$)')

    plt.gcf().text(0.76, 0.5, "\n$N = 300$\n$V = 10 \, m/s$\n$\Delta t = 0.0002 \, s$")

    plt.grid(True)

    plot_cientific_notation(-2, 2, plt)

    plot_set_right_legends(plt)
    plt.tight_layout(pad=1.0)
    plt.subplots_adjust(right=0.75)

    plt.show()

# Ruta al directorio que contiene los archivos CSV
directory = '../compute/msd/v10/'
plot_all_msds(directory, '../compute/msd/v10/msd.csv')
