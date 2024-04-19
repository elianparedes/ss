from compute.pressure import compute
import matplotlib.pyplot as plt
import pandas as pd

df = compute()
plt.figure(figsize=(12, 6))

plt.plot(df['interval'], df['pressure'], linestyle='-')

plt.xlabel('Tiempo (s)')
plt.ylabel('Presión')
plt.grid(True)
plt.tight_layout()
plt.savefig("pressure_vs_dt.png")
plt.show()