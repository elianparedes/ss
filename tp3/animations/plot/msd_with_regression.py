import pandas as pd
import matplotlib.pyplot as plt
from scipy.stats import linregress


def plot_msd_with_regression(csv_filename):
    data = pd.read_csv(csv_filename)

    if 'Time' not in data.columns or 'Mean_MSD' not in data.columns:
        raise ValueError("El archivo CSV debe contener las columnas 'Time' y 'Mean_MSD'")

    plt.figure(figsize=(10, 5))
    plt.plot(data['Time'], data['Mean_MSD'], label='Datos de MSD', linestyle='-', color='b')

    filtered_data = data[(data['Time'] >= 0) & (data['Time'] <= 0.25)]

    slope, intercept, r_value, p_value, std_err = linregress(filtered_data['Time'], filtered_data['Mean_MSD'])
    line = (slope * filtered_data['Time']) + intercept

    plt.plot(filtered_data['Time'], line, label=f'Regresión Lineal: pendiente={slope:.2f}', color='red', linestyle='--')

    plt.title('Desplazamiento Cuadrático Medio (MSD) y Regresión Lineal')
    plt.xlabel('Tiempo (s)')
    plt.ylabel('MSD (unidad^2)')
    plt.legend()
    plt.grid(True)
    plt.show()

    return slope, intercept, r_value, p_value, std_err


csv_filename = '../compute/msd/v3/msd.csv'
slope, intercept, r_value, p_value, std_err = plot_msd_with_regression(csv_filename)
print(f"Coef. Difusión: {slope/4}")
