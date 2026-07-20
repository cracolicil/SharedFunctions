def call(Map cfg = [:]){

    if(!cfg.cartellaScripts?.trim()){
        error "runScripts: cartellaScripts non valorizzato"
    }
}


// Recupera gli script da avviare in sequenza
def listaScript = powershell(
    returnStdout: true,
    script: "(Get-ChildItem -Path '${cfg.cartellaScripts}' -Filter *.ps1).FullName -join ','"
).trim()

// Verifica che gli script esistano
if(!listaScript){
    error "Nessun file .ps1 trovato nella cartella: ${cfg.cartellaScripts}"
}

// Prepara la 'mega' stringa di script in una lista
def scripts = listaScript.split(',')

// Ciclo di esecuzione degli script
for (script in scripts){
    echo "Esecuzione di: ${script}"

    // Si ferma prima di uno script '', nel nostro caso perchè potrebbe esserci bisogno di eseguire istruzioni su DB
    if(script.endsWith('0_rilasciatore_QAS_v6_p2.ps1')){
        runSql(cfg)
    }
    
    // Avvio del prossimo script powershell
    powershell "& '${script}'"
}