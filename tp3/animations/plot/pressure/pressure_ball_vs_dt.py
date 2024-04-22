import matplotlib.pyplot as plt
import pandas as pd
from matplotlib.ticker import ScalarFormatter

from plot.plot_utils import configure_plot_presets, plot_set_right_legends

pressure_ball_df = pd.read_csv('../../compute/pressure/l0.1-n300-i20000-s1-mfalse-pressure_ball.csv')
pressure_df = pd.read_csv('../../compute/pressure/l0.1-n300-i20000-s1-mfalse-pressure.csv')

configure_plot_presets(plt)
plt.plot(pressure_df['interval'], pressure_df['pressure'], linestyle='-', label='Sobre\nparedes')
plt.plot(pressure_ball_df['interval'], pressure_ball_df['pressure'], linestyle='-', label='Sobre\nobstáculo')

plt.xlabel('Tiempo (s)')
plt.ylabel('Presión (Pa·m)')
plot_set_right_legends(plt)
plt.gcf().text(0.82, 0.5, "\n$N = 300$\n$V = 1 m/s$\n$\Delta t = 0.06$\n$It_{max}=20000$")

formatter = ScalarFormatter(useMathText=True)
formatter.set_scientific(True)
formatter.set_powerlimits((-2, 2))
plt.gca().yaxis.set_major_formatter(formatter)

plt.xlim(0, pressure_ball_df['interval'].max())

plt.grid(True)
plt.tight_layout()
plt.savefig("pressure_vs_dt.png")

plt.show()
