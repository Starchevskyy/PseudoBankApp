package com.example.pseudobankapp

import android.app.Application

class PseudoBankApp:Application() {
    override fun onCreate() {
        super.onCreate()
        Graph.provide(this)
        //zde je nadefinovaný kontext ve kterém se má pracovat s databází

    }
}

//---------------------------------------------------------------
//----------------------ZADÁNÍ---------------------------------
//---------------------------------------------------------------
/*

                Vytvořit aplikaci bankovnictví

 a) přihlašovací obrazovka k účtu banky                                 =>       DONE

 [login, heslo], bankovní účet; později databáze                        =>       DONE

 b) hlavní okno - inspirovat v reálné aplikaci bankovnictví             =>       DONE

 funkce: Nová platba,                                                   =>       DONE


 c) nová platba

 zadání platebních údajů
 a potvrzení (odeslání),                                                =>       DONE
 platba se uloží do db                                                  =>       DONE

 údaje: číslo účtu,                                                     =>
 kód banky (seznam název banky a kód banky), VS, SS, KS, zpráva pro příjemce, zpráve pro odesílatele,
 funkce zkopírovat zprávu příjemce do zprávy pro odesítale
 datum splatnosti



 funkce: seznam příjemce a vybrat příjemce ze seznamu

 kontrola dostatku finančních prostředků na účtu                            => Done

 2. krok: přehled zadané platby

 3. krok: odeslání platby                                                    =>Done

 d) historie plateb                                                          => done


 celkem, filtrace v období + celkem za období

 e) příchozí a odchozí platby, filtrace ....

 ---

 */
