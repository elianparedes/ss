import plotly.graph_objs as go
import pandas as pd
import os

path_to_csv_folder = '../../output/va_time/'
csv_files = [file for file in os.listdir(path_to_csv_folder) if file.endswith('.csv')]

traces = []

n_value = None
l_value = None

for csv_file in csv_files:
    file_path = os.path.join(path_to_csv_folder, csv_file)
    data = pd.read_csv(file_path)
    if n_value is None:
        n_value = data['n'].iloc[0]
    if l_value is None:
        l_value = data['l'].iloc[0]
    etha_value = data['etha'].iloc[0]

    trace = go.Scatter(
        x=data['time'],
        y=data['va'],
        mode='lines',
        name=f'etha = {etha_value}'
    )
    traces.append(trace)

layout = go.Layout(
    title=f'va en función de time (N = {n_value}, L = {l_value})',
    xaxis=dict(title='time'),
    yaxis=dict(title='va'),
    legend=dict(title='Valores de etha')
)

fig = go.Figure(data=traces, layout=layout)

fig.show()
