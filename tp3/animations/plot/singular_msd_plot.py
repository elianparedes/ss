import pandas as pd
from matplotlib import pyplot as plt
from scipy.stats import linregress


def plot_msd(csv_filename):
    data = pd.read_csv(csv_filename)

    if 'Time' not in data.columns or 'MSD' not in data.columns:
        raise ValueError("El archivo CSV debe contener las columnas 'Time' y 'MSD'")

    data.sort_values('Time', inplace=True)

    slope, intercept, r_value, p_value, std_err = linregress(data['Time'], data['MSD'])
    line = slope * data['Time'] + intercept

    plt.figure(figsize=(10, 5))
    plt.plot(data['Time'], data['MSD'], label='Datos MSD', linestyle='-', color='b')
    plt.plot(data['Time'], line, label=f'Ajuste Lineal: pendiente={slope:.2f}', color='red', linestyle='--')
    plt.title('Desplazamiento Cuadrático Medio (MSD) vs. Tiempo')
    plt.xlabel('Tiempo (s)')
    plt.ylabel('MSD (unidad^2)')
    plt.legend()
    plt.grid(True)
    plt.show()


csv_filename = '../compute/msd/v1/processed_msd_1.csv'
plot_msd(csv_filename)
