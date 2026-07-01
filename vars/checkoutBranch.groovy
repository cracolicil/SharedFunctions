def call(Map cfg = [:]) {
    if (!cfg.repoUrl) {
        error "checkoutBranch: parametro obbligatorio mancante: repoUrl"
    }

    if (!cfg.credentialsId) {
        error "checkoutBranch: parametro obbligatorio mancante: credentialsId"
    }

    if (!cfg.branchName) {
        error "checkoutBranch: parametro obbligatorio mancante: branchName"
    }
    checkout([
        $class: 'GitSCM',
        branches: [[name: "*/${cfg.branchName}"]],
        userRemoteConfigs: [[
            url: cfg.repoUrl,
            credentialsId: cfg.credentialsId
        ]],
        extensions: [[$class: 'CleanBeforeCheckout']]
    ])
}