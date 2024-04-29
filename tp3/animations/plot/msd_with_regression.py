import numpy as np
import pandas as pd
import matplotlib.pyplot as plt
from scipy.stats import linregress
import plot_utils as pltutils

def plot_msd_with_regression(csv_filename):
    data = pd.read_csv(csv_filename)
    pltutils.configure_plot_presets(plt)

    if 'Time' not in data.columns or 'Mean_MSD' not in data.columns:
        raise ValueError("El archivo CSV debe contener las columnas 'Time' y 'Mean_MSD'")

    plt.plot(data['Time'], data['Mean_MSD'], label='DCM', linestyle='-', color='k')

    filtered_data = data[(data['Time'] >= 0) & (data['Time'] <= 0.25)]

    slope, intercept, r_value, p_value, std_err = linregress(filtered_data['Time'], filtered_data['Mean_MSD'])

    slope_magnitude = np.floor(np.log10(abs(slope)))
    slope_error_magnitude = np.floor(np.log10(abs(std_err)))
    decimals = abs(int(np.ceil(-np.log10(slope))) - int(np.ceil(-np.log10(std_err))))

    slope_scientific_label = f"{slope / 10 ** slope_magnitude:.{decimals-1}f} x 10$^{{{int(slope_magnitude)}}}$"
    error_scientific_label = f"{std_err/10 ** (slope_magnitude):.{decimals-1}f}"

    line = (slope * filtered_data['Time']) + intercept


    plt.plot(filtered_data['Time'], line, label=f'm={slope_scientific_label} ± {error_scientific_label}', color='red', linestyle='--',linewidth=2)

    plt.xlabel('Tiempo (s)')
    plt.ylabel('DCM (m$^2$)')
    plt.legend()

    pltutils.plot_set_right_legends(plt)
    pltutils.plot_cientific_notation(-2,2,plt)

    plt.gcf().text(0.66, 0.5, "\n$N = 300$\n$V = 1 \, m/s$\n$\Delta t = 0.002 \, s$")
    plt.tight_layout(pad=0.5)
    plt.subplots_adjust(right=0.65)

    plt.grid(True)
    plt.show()

    return slope, intercept, r_value, p_value, std_err


csv_filename = '../compute/msd/v1/msd.csv'
slope, intercept, r_value, p_value, std_err = plot_msd_with_regression(csv_filename)

#with open('../compute/msd/slope_speed.csv', 'a') as file:
#    file.write(f"{10},{slope/4},{std_err/4}\n")

print(std_err)
print(f"Coef. Difusión: {slope/4}")
