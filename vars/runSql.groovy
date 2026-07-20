def call(Map cfg = [:]){

    if(!cfg.credentialsId?.trim()){
        error "runSql: credentialsId non valorizzato"
    }
    if(!cfg.server?.trim()){
        error "runSql: server non valorizzato"
    }
    if(!cfg.database?.trim()){
        error "runSql: database non valorizzato"
    }
    if(!cfg.sharedFolderBase?.trim()){
        error "runSql: sharedFolderBase non valorizzato"
    }
    if(!cfg.sharedFolderProject?.trim()){
        error "runSql: sharedFolderProject non valorizzato"
    }
    if(!cfg.version?.trim()){
        error "runSql: version non valorizzato"
    }
    if(!cfg.profile?.trim()){
        error "runSql: version non valorizzato"
    }

    // Recupera lista di tutti i file sql da eseguire in ordine
    def listaSql = powershell(
        returnStdout: true,
        script: """
            (Get-ChildItem -Path '${env.CARTELLA_RILASCIATI}' -Filter *.sql |
            ForEach-Object{
                if ((\$_.Name.Split('_')[0] -as [int]) -eq \$null) 
                    {
                        throw "Errato nome del file: \$(\$_.Name)"
                    } 
                    else
                    {
                        \$_ 
                    }} | 
            Sort-Object {[int](\$_.Name.Split('_')[0])} | 
            ForEach-Object {\$_.FullName }) -Join ','
        """
    ).trim()

    // Se non ci sono istruzioni sql avvisa l'utente
    if (!listaSql) {
        echo "Nessun file .sql trovato nella cartella: ${env.CARTELLA_RILASCIATI}"
    }

    def listaDaEseguire = listaSql.split(',')

    withEnv([
        "SQLCMD_SERVER=${cfg.dbInfo.server}",
        "SQLCMD_DATABASE=${cfg.dbInfo.database}",

    ]){
        withCredentials([
                usernamePassword(
                    credentialsId: "${cfg.dbInfo.credentialsId}",
                    usernameVariable: 'DB_USER',
                    passwordVariable: 'DB_PASS'
                )
            ]) {
            // Ciclo dove verranno eseguite le istruzioni sql
            for (file in listaDaEseguire){
                if(!file)
                {
                    continue
                }
                println "eseguo ${file.split('\\\\')[-1]}:"
                powershell """
                    sqlcmd `
                    -S \$env:SQLCMD_SERVER `
                    -d \$env:SQLCMD_DATABASE `
                    -U \$env:DB_USER `
                    -P "\$env:DB_PASS" `
                    -i ${file} `
                    -b -V 11 -r 1
                """
            }
            println "DB Migration completata"
        }
    }
}