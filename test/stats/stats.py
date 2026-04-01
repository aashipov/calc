import os
import pathlib

import matplotlib.pyplot
import pandas
import seaborn
import statsmodels.api
from seaborn.axisgrid import FacetGrid
from statsmodels.formula.api import ols
from statsmodels.regression.linear_model import RegressionResultsWrapper
from statsmodels.sandbox.stats.multicomp import TukeyHSDResults
from statsmodels.stats.diagnostic import kstest_normal
from statsmodels.stats.multicomp import pairwise_tukeyhsd

PASTE_CSV: str = "paste.csv"
DESCRIPTIVE_STATISTICS_REPORT_CSV: str = "desc.csv"
KOLMOGOROV_SMIRNOV_STATISTIC_REPORT_CSV: str = "ks.csv"
ANOVA_REPORT_CSV: str = "anova.csv"
TUKEY_HSD_REPORT_CSV: str = "tukeyhsd.csv"
CATPLOT_REPORT_PNG: str = "catplot.png"
DELIMITER_COMMA: str = ","
DELIMITER_NEW_LINE = "\n"


def read_csv(filename: os.PathLike[str]) -> pandas.DataFrame:
    # load data file
    return pandas.read_csv(filename, sep=DELIMITER_COMMA)


def to_string(df: pandas.DataFrame) -> str:
    return df.to_csv(sep=DELIMITER_COMMA) + DELIMITER_NEW_LINE


def write_csv(filename: os.PathLike[str], content: str) -> None:
    with open(filename, "w") as f:
        _ = f.write(content)


def melt_df(df: pandas.DataFrame) -> pandas.DataFrame:
    # reshape the dataframe suitable for statsmodels package
    return pandas.melt(df.reset_index(), id_vars=["index"], value_vars=list(df.columns))


def do_descriptive_stats(df: pandas.DataFrame) -> None:
    # Descriptive statistics
    desc: pandas.DataFrame = df.describe()
    csv: pathlib.Path = pathlib.Path(DESCRIPTIVE_STATISTICS_REPORT_CSV)
    content: str = to_string(desc)
    write_csv(csv, content)


def do_ks(df_melt: pandas.DataFrame) -> None:
    # Kolmogorov-Smirnov test, whether sample variance distribution is normal
    ks = kstest_normal(df_melt["value"])
    csv: pathlib.Path = pathlib.Path(KOLMOGOROV_SMIRNOV_STATISTIC_REPORT_CSV)
    content: str = DELIMITER_COMMA.join(str(elem) for elem in ks) + DELIMITER_NEW_LINE
    write_csv(csv, content)


def do_anova(df_melt: pandas.DataFrame) -> None:
    # ANOVA
    model: RegressionResultsWrapper = ols("value ~ C(variable)", data=df_melt).fit()
    anova_table: pandas.DataFrame = statsmodels.api.stats.anova_lm(model, typ=2)
    csv: pathlib.Path = pathlib.Path(ANOVA_REPORT_CSV)
    content: str = to_string(anova_table)
    write_csv(csv, content)


def do_tukey(df_melt: pandas.DataFrame) -> None:
    # Tukey HSD test, if there is difference between groups
    # Reject === True means 'reject null hypothesis', there is difference for alpha=0.05
    tukey: TukeyHSDResults = pairwise_tukeyhsd(
        endog=df_melt["value"], groups=df_melt["variable"], alpha=0.05
    )
    csv: pathlib.Path = pathlib.Path(TUKEY_HSD_REPORT_CSV)
    content: str = tukey.summary().as_csv() + "\n"
    write_csv(csv, content)


def count_lines(filename: os.PathLike[str]) -> int:
    with open(filename, "r") as f:
        return sum(1 for _ in f)


def do_catplot(df_melt: pandas.DataFrame, num_lines: int) -> None:
    cp: FacetGrid = seaborn.catplot(data=df_melt, kind="bar", x="value", y="variable")
    _ = cp.set_axis_labels(
        "Request elapsed time (the smaller the better), top "
        + str(num_lines - 1)
        + " records",
        "Implementation",
    )
    for i in range(0, 30, 5):
        _ = cp.ax.axvline(x=i + 5, color="red", linestyle="--")
    matplotlib.pyplot.gcf().set_size_inches(10, 5)
    matplotlib.pyplot.savefig(CATPLOT_REPORT_PNG)


# Based on https://www.reneshbedre.com/blog/anova.html
def main() -> None:
    source_file: pathlib.Path = pathlib.Path(PASTE_CSV)
    num_lines: int = count_lines(source_file)
    df: pandas.DataFrame = read_csv(source_file)
    do_descriptive_stats(df)
    df_melt: pandas.DataFrame = melt_df(df)
    do_ks(df_melt)
    do_anova(df_melt)
    do_tukey(df_melt)
    do_catplot(df_melt, num_lines)


if __name__ == "__main__":
    main()
