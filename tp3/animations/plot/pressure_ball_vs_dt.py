import matplotlib.pyplot as plt
import pandas as pd

pressure_ball_df = pd.read_csv('../compute/pressure_ball.csv')
pressure_df = pd.read_csv('../compute/pressure.csv')
plt.figure(figsize=(12, 6))

plt.plot(pressure_ball_df['interval'], pressure_ball_df['pressure'], linestyle='-')
plt.plot(pressure_df['interval'], pressure_df['pressure'], linestyle='-')

# Calculate and plot the mean of both lines
mean_pressure_ball = pressure_ball_df['pressure'].mean()
mean_pressure = pressure_df['pressure'].mean()
plt.axhline(mean_pressure_ball, color='b', linestyle='--', label=f'Mean Pressure Ball: {mean_pressure_ball:.2f}')
plt.axhline(mean_pressure, color='orange', linestyle='--', label=f'Mean Pressure: {mean_pressure:.2f}')

plt.xlabel('Tiempo (s)')
plt.ylabel('Presión en la bocha')
plt.grid(True)
plt.tight_layout()
plt.savefig("pressure_vs_dt.png")

plt.show()