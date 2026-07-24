package distillatore;

/**
 * <p>Title: Distillatore</p>
 * <p>Description: Distilla le informazioni essenziali contenute nei comandi Show dell'IOS</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Lutech SPA</p>
 * @author Faustino Palma
 * @version 1.1
 */

public class xml {
/*
<?xml version="1.0"?>
<analisi>
  <item name="Event List">
  </item>
  <item name="Risorse">
  </item>
  <item name="Modalita Operative">
  </item>
  <item name="Address Plan">
  </item>
  <item name="Elenco Risultati Anomali">
  </item>
  <item name="Network Analysis Project">
  </item>
  <item name="Network Analisys Procedure">
  </item>
  <item name="Topology Analisis">
    <item name="Topologia WAN">
    </item>
    <item name="Topologia LAN">
    </item>
    <item name="Struttura Campus">
    </item>
    <item name="Collegamenti Ridontanti">
    </item>
  </item>
  <item name="Address Plan Analisys">
  </item>
  <item name="Configurations Analisys">
    <item name="Servizio di Logging">
      <item name="buffered">
      </item>
      <item name="server">
      </item>
      <item name="service timestamps log datetime localtime">
      </item>
    </item>
    <item name="HostName">
      <item name="risolto dal DNS">
      </item>
    </item>
    <item name="Description Interface">
    </item>
    <item name="Numerazione Logica delle SubInterface">
    </item>
    <item name="Traps Specifiche">
    </item>
    <item name="VLANs">
    </item>
    <item name="NTP">
      <item name="presenza NTP">
      </item>
    </item>
    <item name="ConfReg">
    </item>
    <item name="Interface LoopBack">
    </item>
    <item name="Address Plan">
    </item>
  </item>
  <item name="Routing Analisys">
    <item name="Protocolli in uso Elenco AS">
    </item>
    <item name="Redistribuzioni">
    </item>
    <item name="Summarizzazione">
    </item>
    <item name="Matrici di Traffico">
    </item>
    <item name="Policy Schema">
    </item>
  </item>
  <item name="Switching Analisys">
    <item name="VLANs CED-STP">
    </item>
    <item name="Switch Block">
    </item>
    <item name="Core Block">
    </item>
    <item name="Layer 2 Schema">
    </item>
    <item name="STP Trunking & port channel">
    </item>
  </item>
  <item name="Security Analisys">
    <item name="Sistema AAA">
      <item name="Tacacs SI_NO">
      </item>
    </item>
    <item name="Sicurezza di base sulle Configurazioni">
      <item name="ip http server">
      </item>
      <item name="ip finger">
      </item>
      <item name="service tcp-small-servers">
      </item>
      <item name="service udp-small-servers">
      </item>
      <item name="service pad">
      </item>
      <item name="ip bootp server">
      </item>
      <item name="service password-encryption">
      </item>
    </item>
    <item name="Zone Critiche">
    </item>
    <item name="Traffico non consentito">
    </item>
    <item name="Bug Reports">
    </item>
    <item name="Tipologie di Accesso Cliente">
    </item>
    <item name="Protezione Accesso Console VTY AUX">
      <item name="PW Console">
      </item>
      <item name="PW Aux">
      </item>
      <item name="PW Vty">
      </item>
    </item>
    <item name="Autenticazione Protocolli">
      <item name="Autenticazione PAP sui backup">
      </item>
      <item name="Autenticazione CHAP sui backup">
      </item>
      <item name="Autenticazione HSRP">
      </item>
      <item name="Autenticazione NTP">
      </item>
    </item>
    <item name="Altre Features">
    </item>
  </item>
  <item name="Software Release Analisys">
    <item name="Release in opera sulla rete">
    </item>
    <item name="Verifica bugs gravi">
    </item>
    <item name="IOS reccomandation pre sincronizzazione">
    </item>
    <item name="FeedBack per bug Fixing">
    </item>
    <item name="Verifica dimensinamento apparati">
    </item>
  </item>
  <item name="High Availability">
    <item name="Diversificazione L1 L2">
    </item>
    <item name="Diversificazione L3">
    </item>
    <item name="Verifica bilanciamenti di carico">
    </item>
    <item name="Ridondanza HW">
    </item>
  </item>
  <item name="Backup Services">
    <item name="Tipologie di BackUp">
    </item>
    <item name="Configurazione BackUp lato Concentratori">
    </item>
    <item name="Controllo Limiti Adiacenze">
    </item>
    <item name="Test Campione">
    </item>
  </item>
  <item name="Hardware Performance">
    <item name="Monitoring stato CPU">
    </item>
    <item name="Utilizzo memoria">
    </item>
    <item name="Errori Hardware sui Log">
    </item>
    <item name="Parametri Enviromental">
    </item>
  </item>
  <item name="Load Analisys">
    <item name="Carico interfacce WAN e LAN">
    </item>
    <item name="Carico buffer">
    </item>
    <item name="Carico in condizioni di BackUP">
    </item>
    <item name="Traffico protocolli">
    </item>
  </item>
  <item name="Protocol Utilization">
    <item name="Protocolli di routing">
    </item>
    <item name="Protocolli di switching">
    </item>
    <item name="Protocolli di interconnessione LAN WAN">
    </item>
    <item name="Compatibilità dei protocolli utilizzati">
    </item>
  </item>
  <item name="Log Analisys">
    <item name="Analisi Log apparati Ced">
    </item>
    <item name="Analisi Log apparati Concentratori">
    </item>
    <item name="Analisi Log apparati Filiali">
    </item>
  </item>
  <item name="DLSW Analisys">
    <item name="Protocolli">
      <item name="DLSW">
      </item>
      <item name="SDLC">
      </item>
      <item name="Token Ring">
      </item>
      <item name="Ethernet">
      </item>
      <item name="X25">
      </item>
    </item>
    <item name="Filtri">
    </item>
    <item name="Distribution Peer">
      <item name="peer principale">
      </item>
      <item name="peer secondario">
      </item>
    </item>
  </item>
  <item name="Mappe">
  </item>
  <item name="CED">
  </item>
  <item name="Concentratori">
  </item>
  <item name="Filiali">
  </item>
</analisi>
*/
}