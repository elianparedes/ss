import matplotlib.pyplot as plt

def set_global_font_size(size):
    plt.rc('font', size=size)
    plt.rc('axes', titlesize=size)
    plt.rc('axes', labelsize=size)
    plt.rc('xtick', labelsize=size)
    plt.rc('ytick', labelsize=size)
    plt.rc('legend', fontsize=size)
    plt.rc('figure', titlesize=size)


def set_plot_size(fig, width, height):
    fig.set_size_inches(width, height)


class PlotLegend:
    def __init__(self, legend):
        self.legend = legend

    @staticmethod
    def get_legend(ax):
        return PlotLegend(ax.legend())

    def set_title(self, title):
        self.legend.set_title(title)
        return self

    def set_legend_outside(self, loc='center left', bbox_to_anchor=(1.05, 0.5)):
        self.legend.set_bbox_to_anchor(bbox_to_anchor)
        self.legend.set_loc(loc)
        return self

    def set_title_font_size(self, size):
        self.legend.get_title().set_fontsize(size)
        return self

    def set_title_bold(self):
        self.legend.get_title().set_fontweight('bold')
        return self

    def set_title_horizontal_alignment(self, align):
        self.legend._legend_box.align = align
        return self
