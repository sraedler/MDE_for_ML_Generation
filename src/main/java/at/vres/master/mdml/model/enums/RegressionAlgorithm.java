package at.vres.master.mdml.model.enums;

public enum RegressionAlgorithm {
    LINEAR("LinearRegression"),
    LASSO("LassoRegression"),
    LOGISTIC("LogisticRegression"),
    RANDOM_FOREST("RandomForestRegressor"),
    DECISION_TREE("DecisionTreeRegressor");

    private final String regressionAlgorithm;

    RegressionAlgorithm(String regressionAlgorithm) {
        this.regressionAlgorithm = regressionAlgorithm;
    }

    public String getRegressionAlgorithm() {
        return regressionAlgorithm;
    }
}
