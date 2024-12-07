# Based on https://www.reneshbedre.com/blog/anova.html

import os
import pathlib

import matplotlib.pyplot
import pandas
import seaborn
import statsmodels.api
from statsmodels.formula.api import ols
from statsmodels.stats.diagnostic import kstest_normal
from statsmodels.stats.multicomp import pairwise_tukeyhsd


def read_csv(filename: os.PathLike) -> pandas.DataFrame:
    # load data file
    return pandas.read_csv("paste.csv", sep=",")


def melt_df(df: pandas.DataFrame) -> pandas.DataFrame:
    # reshape the d dataframe suitable for statsmodels package
    return pandas.melt(df.reset_index(), id_vars=["index"], value_vars=list(df.columns))


def describe(df: pandas.DataFrame) -> None:
    desc = df.describe()
    with open("desc.csv", "w") as f:
        f.write(desc.to_csv() + "\n")


def do_ks(df_melt: pandas.DataFrame) -> None:
    # Kolmogorov-Smirnov test, whether sample variance distribution is normal
    ks = kstest_normal(df_melt["value"])
    with open("ks.csv", "w") as f:
        f.write(str(ks) + "\n")


def do_anova(df_melt: pandas.DataFrame) -> None:
    # ANOVA
    model = ols("value ~ C(variable)", data=df_melt).fit()
    anova_table = statsmodels.api.stats.anova_lm(model, typ=2)
    with open("anova.csv", "w") as f:
        f.write(anova_table.to_csv() + "\n")


def do_tukey(df_melt: pandas.DataFrame) -> None:
    # Tukey HSD test, if there is difference between groups
    # Reject === True means 'reject null hypothesis', there is difference for alpha=0.05
    tukey = pairwise_tukeyhsd(
        endog=df_melt["value"], groups=df_melt["variable"], alpha=0.05
    )
    with open("tukeyhsd.csv", "w") as f:
        f.write(tukey.summary().as_csv() + "\n")


def do_catplot(df_melt: pandas.DataFrame) -> None:
    with open("paste.csv", "r") as f:
        num_lines = sum(1 for _ in f)
    cp = seaborn.catplot(data=df_melt, kind="bar", x="value", y="variable")
    cp.set_axis_labels(
        "Request elapsed time (the lower the better), top "
        + str(num_lines - 1)
        + " records",
        "Implementation",
    )
    matplotlib.pyplot.gcf().set_size_inches(10, 5)
    matplotlib.pyplot.savefig("catplot.png")


def main() -> None:
    df: pandas.DataFrame = read_csv(pathlib.Path("paste.csv"))
    describe(df)
    df_melt = melt_df(df)
    do_ks(df_melt)
    do_anova(df_melt)
    do_tukey(df_melt)
    do_catplot(df_melt)


if __name__ == "__main__":
    main()
