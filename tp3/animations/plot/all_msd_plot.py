import os
import pandas as pd
import numpy as np
from matplotlib import pyplot as plt
from scipy.stats import linregress

def plot_all_msds(directory,output_csv_file):
    all_data = []  # Lista para almacenar los datos de cada archivo

    # Recorrer todos los archivos en el directorio especificado
    for filename in os.listdir(directory):
        if filename.endswith('.csv'):
            csv_path = os.path.join(directory, filename)
            data = pd.read_csv(csv_path)

            if 'Real Time' not in data.columns or 'MSD' not in data.columns:
                continue  # Saltar archivos que no tengan las columnas necesarias

            data.sort_values('Real Time', inplace=True)
            all_data.append(data)
            plt.plot(data['Real Time'], data['MSD'], '--', label=f'MSD {filename}', linewidth=1)

    # Calcular el promedio de MSD en cada tiempo si hay archivos procesados
    if all_data:
        # Concatenar todos los DataFrames y calcular el promedio
        all_data_df = pd.concat(all_data).groupby('Real Time').mean().reset_index()
        print(all_data_df)
        plt.plot(all_data_df['Real Time'], all_data_df['MSD'], 'k-', label='Promedio de MSD', linewidth=2)

    mean_msd_df = pd.DataFrame({
        'Time': all_data_df['Real Time'],
        'Mean_MSD': all_data_df['MSD']
    })
    mean_msd_df.to_csv(output_csv_file, index=False)

    # Configurar el gráfico
    plt.title('Desplazamiento Cuadrático Medio (MSD) vs. Tiempo para Varios Archivos')
    plt.xlabel('Tiempo (s)')
    plt.ylabel('MSD (unidad^2)')
    plt.legend()
    plt.grid(True)
    plt.show()

# Ruta al directorio que contiene los archivos CSV
directory = '../compute/msd/v10/'
plot_all_msds(directory, '../compute/msd/v10/msd.csv')
