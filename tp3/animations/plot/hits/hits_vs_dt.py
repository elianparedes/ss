import matplotlib.pyplot as plt
import pandas as pd

from plot.plot_utils import configure_plot_presets

hits_df = pd.read_csv('../../compute/hits/output/v10/hits_30k_1.csv')

configure_plot_presets(plt)
plt.plot(hits_df['interval'], hits_df['all_hits'], linestyle='-', label='Colisiones', color='purple')

plt.xlabel('Tiempo (s)')
plt.ylabel('Nro. de colisiones contra el obstáculo')

plt.gcf().text(0.86, 0.5, "\n$N = 300$\n$V = 10 m/s$\n$It_{max}=30000$")
plt.grid(True)

plt.tight_layout()
plt.subplots_adjust(right=0.85)
plt.savefig("hits_vs_dt_v10_unique.png")

plt.show()
