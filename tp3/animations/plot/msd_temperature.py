import matplotlib.pyplot as plt
import pandas as pd

import plot_utils as pltutils

pltutils.configure_plot_presets(plt)
pltutils.plot_scientific_notation(-2, 2, plt)
df = pd.read_csv('../compute/msd/slope_speed.csv')

df['T'] = df['speed'] ** 2

colors = ['blue', 'green', 'red', 'purple']

for i, row in df.iterrows():
    plt.errorbar(row['T'], row['d'], yerr=row['error'], fmt='o', ecolor=colors[i], capsize=3, linestyle='None',
                 marker='o', markersize=8, color=colors[i], label=f'T={row["T"]}')

plt.xlabel('Temperatura (U.A)')
plt.ylabel('Coef. de Difusión')

plt.legend(title="Temperaturas")

plt.gcf().text(0.81, 0.70, "\n$N = 300$")
plt.tight_layout(pad=0.5)
plt.subplots_adjust(right=0.8)

pltutils.plot_set_right_legends(plt)

plt.grid(True)

plt.show()
