import os
import pandas as pd
import plotly.express as px

def va_time(directory_path):
    all_data = []

    for filename in os.listdir(directory_path):
        if filename.endswith(".csv"):
            file_path = os.path.join(directory_path, filename)
            data = pd.read_csv(file_path)
            etha_value = data['etha'].iloc[0]
            data['etha_value'] = etha_value
            all_data.append(data)

    combined_data = pd.concat(all_data)
    etha_values_sorted = sorted(combined_data['etha_value'].unique())

    fig = px.line(combined_data, x='time', y='va', color='etha_value', labels={'etha_value': 'etha'},
                  category_orders={'etha_value': etha_values_sorted})
    fig.update_layout(font=dict(family="Arial", size=11))
    fig.show()

directory_path = '../../output/va_time/'
va_time(directory_path)
