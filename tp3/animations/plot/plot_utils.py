from matplotlib.ticker import ScalarFormatter


def configure_plot_presets(plt):
    plt.figure(figsize=(14, 8))
    plt.rcParams.update({'font.size': 20})


def plot_scientific_notation(minPower, maxPower, plt):
    formatter = ScalarFormatter(useMathText=True)
    formatter.set_scientific(True)
    formatter.set_powerlimits((minPower, maxPower))
    plt.gca().yaxis.set_major_formatter(formatter)


def plot_set_right_legends(plt):
    plt.legend(loc='upper left', bbox_to_anchor=(1, 1))
