def call(Map cfg = [:]) {
    if(!cfg.endPoint) {
        error "parametro obbligatorio mancante: endPoint"
    }
    if(!cfg.testSuite) {
        error "parametro obbligatorio mancante: testSuite"
    }
    if(!cfg.propertiesFile) {
        error "parametro obbligatorio mancante: propertiesFile"
    }
    if(!cfg.projectFile) {
        error "parametro obbligatorio mancante: projectFile"
    }

    bat """
        "%TESTRUNNER_PATH%\\testrunner.bat" ^
        -e\${#TestSuite#${cfg.endPoint}} ^
        -r -J -s${cfg.testSuite} -S ^
        -PprojectPropertiesPath="%WORKSPACE%\\${cfg.propertiesFile}" ^
        -PprojectPathTestRunner="%WORKSPACE%" ^
        "%WORKSPACE%\\${cfg.projectFile}"
    """
}