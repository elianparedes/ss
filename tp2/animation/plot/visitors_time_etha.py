import pandas as pd
import matplotlib.pyplot as plt

from lib.file.csv import get_most_recent_csv

visitors_time_etha_file = get_most_recent_csv('../../output/visitors-time-etha/')
data = pd.read_csv(visitors_time_etha_file)

df = pd.DataFrame(data)

# Plotting
plt.errorbar(df['etha'], df['mean'], yerr=df['stdev'], fmt='o', capsize=5, label='N=300, L=5')
plt.xlabel('Etha')
plt.ylabel('Time for visited count reaching 0.6 of total particles')
plt.title('Time vs Etha')
plt.legend()
plt.grid(True)
plt.show()