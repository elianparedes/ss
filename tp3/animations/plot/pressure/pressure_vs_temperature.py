import pandas as pd
from matplotlib import pyplot as plt
from matplotlib.ticker import ScalarFormatter, AutoLocator

from plot.plot_utils import configure_plot_presets, plot_scientific_notation, plot_set_right_legends

# Pressure
pressure_files = [
    '../../compute/pressure/l0.1-n300-i20000-s1-mfalse-pressure.csv',
    '../../compute/pressure/l0.1-n300-i20000-s3-mfalse-pressure.csv',
    '../../compute/pressure/l0.1-n300-i20000-s6-mfalse-pressure.csv',
    '../../compute/pressure/l0.1-n300-i20000-s10-mfalse-pressure.csv'
]

pressure_dfs = []
for file in pressure_files:
    pressure_dfs.append(pd.read_csv(file))

pressure_means = []
pressure_stds = []
for pressure_df in pressure_dfs:
    pressure_means.append(pressure_df['pressure'].mean())
    pressure_stds.append(pressure_df['pressure'].std())

# Pressure over ball
pressure_ball_files = [
    '../../compute/pressure/l0.1-n300-i20000-s1-mfalse-pressure_ball.csv',
    '../../compute/pressure/l0.1-n300-i20000-s3-mfalse-pressure_ball.csv',
    '../../compute/pressure/l0.1-n300-i20000-s6-mfalse-pressure_ball.csv',
    '../../compute/pressure/l0.1-n300-i20000-s10-mfalse-pressure_ball.csv'
]

pressure_ball_dfs = []
for file in pressure_ball_files:
    pressure_ball_dfs.append(pd.read_csv(file))

pressure_ball_means = []
pressure_ball_stds = []
for pressure_df in pressure_ball_dfs:
    pressure_ball_means.append(pressure_df['pressure'].mean())
    pressure_ball_stds.append(pressure_df['pressure'].std())


speeds = [1, 3, 6, 10]

temperatures = []
for speed in speeds:
    temperatures.append(speed ** 2)

configure_plot_presets(plt)

plt.errorbar(x=temperatures, y=pressure_means, yerr=pressure_stds, capsize=5, label='Sobre las paredes $P$', fmt='o', elinewidth=2, markersize='8')
plt.errorbar(x=temperatures, y=pressure_ball_means, yerr=pressure_ball_stds, capsize=5, label='Sobre el obstáculo $P_o$', fmt='o', elinewidth=2,  markersize='8')

plt.xlabel('Temperatura (U.A)')
plt.ylabel('Presión (Pa·m)')


plt.gca().xaxis.set_major_locator(AutoLocator())  # Adjust the interval as needed

plot_scientific_notation(-2, 2, plt)
plot_set_right_legends(plt)
plt.gcf().text(0.78, 0.5, "\n$N = 300$\n$V \in \{1 ,3, 6, 10\} \, (m/s)$")

plt.xticks(temperatures)
plt.grid(True)
plt.tight_layout()
plt.savefig("pressure_vs_temperature.png")

plt.show()