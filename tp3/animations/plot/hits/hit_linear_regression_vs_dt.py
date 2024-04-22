import glob

import matplotlib.pyplot as plt
import numpy as np
import pandas as pd
from scipy.stats import linregress

from plot.plot_utils import plot_set_right_legends


def plot_linear_regression(polies, intervals, hits):
    plt.figure(figsize=(14, 10))
    plt.rcParams.update({'font.size': 20})

    slopes = []
    std_errors = []

    for (index, (p, x, y)) in enumerate(zip(polies, intervals, hits)):
        plt.plot(x, y, '--', linewidth=1, label=f'Colisiones {index + 1}')
        # plt.plot(x, p(x), color='red', label='Linear regression')

        slope, intercept, r_value, p_value, std_err = linregress(x, y)
        slopes.append(slope)
        std_errors.append(std_err)

    mean_slope = np.mean(slopes)
    mean_std_error = np.mean(std_errors)

    print(mean_slope, mean_std_error)

    mean_coefficients = np.mean([p.coefficients for p in polies], axis=0)
    mean_poly = np.poly1d(mean_coefficients)
    plt.plot(intervals[0], mean_poly(intervals[0]), label=f'$m_{{ct}}=${mean_slope:.1f} ± {mean_std_error:.1f}',
             color='red', linestyle='--', linewidth='3')

    plot_set_right_legends(plt)
    plt.gcf().text(0.74, 0.5, f"\n$N = 300$\n$V = 1 \, m/s$\n$\Delta t = {intervals[0][1]:.3f} \, s$")
    plt.xlabel('Tiempo (s)')
    plt.ylabel('Nro. de colisiones vs tiempo')
    plt.tight_layout()
    plt.grid(True)
    plt.savefig('hit_linear_regression_vs_dt.png')
    plt.show()


csv_files = glob.glob("../../compute/hits/output/v1/*.csv")

polies = []
hits = []
intervals = []
for file in csv_files:
    df = pd.read_csv(file)
    x = df['interval']
    y = df['all_hits']
    coefficients = np.polyfit(x, y, 1)
    poly = np.poly1d(coefficients)
    polies.append(poly)
    intervals.append(x)
    hits.append(y)

# df = pd.read_csv(csv_files[1])
# x = df['interval']
# y = df['all_hits']
# coefficients = np.polyfit(x, y, 1)
# poly = np.poly1d(coefficients)
# polies.append(poly)
# intervals.append(x)
# hits.append(y)

plot_linear_regression(polies, intervals, hits)
