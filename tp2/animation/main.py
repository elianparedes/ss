import glob
import pandas as pd
import plotly.express as px
import plotly.graph_objects as go

# Specify the folder path containing the CSV files
folder_path = '../output/'

# Use glob to get a list of all CSV files in the folder
csv_files = glob.glob(folder_path + '*.csv')

# Create an empty figure
fig = px.line()

# Loop through each CSV file
for idx, file in enumerate(csv_files):
    # Read the CSV file into a DataFrame
    df = pd.read_csv(file)

    # Add a line trace for each file with 'va' on the y-axis and 'time' on the x-axis
    fig.add_trace(
        go.Scatter(x=df['time'], y=df['va'], mode='lines', name=f'Etha: {df["etha"][0]}',
                   line=dict(color=px.colors.qualitative.Plotly[idx]))
    )

# Update layout and show the figure
fig.update_layout(xaxis_title='Time', yaxis_title='VA', legend_title='Etha')
fig.show()
