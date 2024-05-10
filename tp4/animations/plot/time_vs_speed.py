import matplotlib.pyplot as plt
import csv

import matplotlib.ticker as ticker

import utils.plots as plots

# Reemplaza esta parte con la ruta de tu archivo CSV
csv_file = '../../output/min-distance-speed.csv'

speeds = []
iterations_multiplied = []

# Lee el archivo CSV y llena las listas
with open(csv_file, newline='') as file:
    reader = csv.DictReader(file)
    for row in reader:
        distance = float(row['distance'])
        if distance < 10**4:
            speeds.append(float(row['speed']))
            iterations_multiplied.append(int(row['iteration']) * 10)

# Crea el gráfico
fig, ax = plt.subplots()
plt.scatter(speeds, iterations_multiplied, color='blue')
plots.set_plot_size(fig,10,6)
plots.set_global_font_size(18)
ax.set_xlabel('Speed')
ax.set_ylabel('Tiempo (s)')

ax.xaxis.set_major_formatter(ticker.ScalarFormatter(useMathText=True))
ax.xaxis.set_major_formatter(ticker.FormatStrFormatter('%.3f'))

for x in speeds:
    ax.axvline(x=x, color='gray', linestyle='--', linewidth=0.5)
for y in iterations_multiplied:
    ax.axhline(y=y, color='gray', linestyle='--', linewidth=0.5)

ax.grid(False)

plt.tight_layout()
plt.show()
