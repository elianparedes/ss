import matplotlib.pyplot as plt
import pandas as pd
from matplotlib.ticker import ScalarFormatter, MultipleLocator

pressure_ball_df = pd.read_csv('../compute/pressure_ball.csv')
pressure_df = pd.read_csv('../compute/pressure.csv')
plt.figure(figsize=(12, 6))

plt.plot(pressure_ball_df['interval'], pressure_ball_df['pressure'], linestyle='-', label='Sobre paredes')
plt.plot(pressure_df['interval'], pressure_df['pressure'], linestyle='-', label='Sobre obstáculo')

# mean_pressure_ball = pressure_ball_df['pressure'].mean()
# mean_pressure = pressure_df['pressure'].mean()
# plt.axhline(mean_pressure_ball, color='b', linestyle='--', label=f'Mean Pressure Ball: {mean_pressure_ball:.2f}')
# plt.axhline(mean_pressure, color='orange', linestyle='--', label=f'Mean Pressure: {mean_pressure:.2f}')

plt.xlabel('Tiempo (s)')
plt.ylabel('Presión')
plt.legend(fontsize=14, title_fontsize=14, loc='center left', bbox_to_anchor=(1, 0.5), title='<MISSING_TITLE>', alignment='left')

formatter = ScalarFormatter(useMathText=True)
formatter.set_scientific(True)
formatter.set_powerlimits((-2, 2))  # Adjust these limits as needed
plt.gca().yaxis.set_major_formatter(formatter)

plt.gca().xaxis.set_major_locator(MultipleLocator(0.01))  # Adjust the interval as needed
plt.xlim(0, pressure_ball_df['interval'].max())

plt.grid(True, which='both', axis='x')
plt.tight_layout()
plt.savefig("pressure_vs_dt.png")

plt.show()