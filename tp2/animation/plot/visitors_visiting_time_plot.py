import pandas as pd
import matplotlib.pyplot as plt

from lib.file.csv import get_most_recent_csv

visitors_file = get_most_recent_csv('../../output/visitors/', contains="rates")
df = pd.read_csv(visitors_file)

plt.figure(figsize=(10, 6))

plt.plot(df['time'], df['visited_count'], linestyle='-', label='Visited Count')

plt.xlabel('Time')
plt.ylabel('Visited Count')
plt.title('Visited Count Over Time')
plt.legend()

plt.grid(True)
plt.tight_layout()
plt.show()