<!--
  🇮🇹 Per la versione in ITALIANO, vai alla sezione "Italiano" più in basso:
     https://github.com/faustinopalma/cantina#italiano
  The English version comes first, the Italian version follows.
-->

# Cantina — Network Configuration Assessment Toolchain

A small suite of Java tools, written in the early 2000s (the source files carry a
2002 copyright), built to **automate the quality assessment of large corporate
networks**. The project name and its modules borrow terminology from
winemaking, because the pipeline mirrors the way grapes become a finished
spirit: you harvest the raw material, you distil it, and you obtain something
refined and ready to be tasted by an expert.

An interactive, bilingual overview of the architecture is available in
[docs/architecture.html](docs/architecture.html) (open it in a browser).

---

## The story

Back then I was working as a network engineer for a system-integration and
consulting company. Together with a team of engineers, I was assigned to a large
project for a major telecommunications operator. Several companies had been
enrolled for the same job, so a handful of teams were effectively competing
against each other, measured on speed and quality. Whoever performed best would
keep the contract.

The task itself was clear, and repetitive: sit in front of a terminal all day
and, one device at a time, connect to each router in a client network through a
text-based interface, download its configuration, run a fixed sequence of
diagnostic commands, read the output, and spot any configuration mistake. The
final deliverable was a report listing the defects found across the network. The
work was entirely individual and required no human interaction.

Many people considered automating it, but almost nobody believed it was
feasible: the configurations were plain, amorphous text written in a rich,
exception-heavy device language. Two configuration files were rarely identical —
different IP addressing, different firmware versions, and the fact that the same
behaviour could be expressed in slightly different ways.

### A bold decision

I had no software-development experience, but I was convinced that the tedious
part of the job could be automated, leaving the engineer free to add the
high-level judgement that really mattered. My idea was a tool that would produce
a **semi-finished report** — a solid starting point an engineer could complete
with insight and context.

A friend who was an experienced programmer recommended Java. I studied it,
proposed the plan to management, and committed to delivering a working tool in
six weeks. I asked to build it instead of doing the manual job, and to be given
a small cross-company team. The client sponsor and my management supported the
idea, though only one of the other companies believed enough to lend a person.

### During the build

The very first thing I did was order a Java book — before I even had the formal
green light — because international shipping to Italy took about a week at the
time.

After a series of meetings with colleagues and the client to shape the
architecture, I worked on the solution day and night. The effort was split: a
colleague took the **connection module**, while I built the **analysis and
output module**. Within three weeks the analysis part was already crunching a
small sample of configuration files, and I handed the first release to the team.
It was buggy, limited, and rough — but it proved the concept, and I was allowed
to continue.

The connection module, unfortunately, was not working, and my partner stepped
away from his part. I took over his component as well. A viable release,
including the connection module, was ready in about seven weeks — only slightly
later than promised. From there the team started using it daily, reporting bugs
and, increasingly, helping to improve it.

---

## The solution

By agreement with the team, the components were named after winemaking.

- **`vendemmiatore`** (*the vintager*) — the harvesting module. It connects to a
  client network through the text-based interface, starts from a single device,
  discovers its neighbours, and walks the entire network, downloading every
  configuration and diagnostic file. The resulting archive of raw files was
  called **`vinaccia`** (the pomace: the raw grape material ready to be worked).

- **`distillatore`** (*the distiller*) — the analysis and output module. It takes
  the `vinaccia` as input and produces a set of summary documents in HTML,
  collectively called **`grappa`**. Built around the **regular-expressions**
  framework (the same kind of pattern matching used to work through DNA
  sequences), the distiller turns messy text into structured, human-readable
  reports — a semi-finished deliverable the engineers could refine into a full
  assessment.

- **`Topology`** — a companion visualiser. From the discovered network it
  computes node centrality (shortest-path distances) and renders an **SVG**
  network diagram, plus an HTML list of devices ranked by centrality.

The most valuable part of the `grappa` was a **cluster analysis** of the device
configurations. The distiller compared configurations across all devices and
grouped them into clusters of similar ones. Similarity was not a naive
text-string comparison: it was a structured function, built on regular
expressions, that ignored the parts that legitimately differ (such as specific
IP addresses) and focused on the structural shape of the configuration — all of
which stayed hidden from the engineer. With the clusters in hand, an engineer no
longer had to inspect every file individually; they could focus on the few
distinct clusters that emerged, making mistakes far easier to spot.

---

## Architecture at a glance

```
  HARVEST                    DISTIL                         VISUALISE
  ┌───────────────┐          ┌──────────────────┐          ┌───────────────┐
  │ vendemmiatore │  ─────▶  │   distillatore   │  ─────▶  │   Topology    │
  │  (telnet,     │ vinaccia │  (regex parsing, │  graph   │  (centrality, │
  │   discovery)  │  (files) │   clustering)    │          │   SVG output) │
  └───────────────┘          └──────────────────┘          └───────────────┘
                                     │ grappa
                                     ▼
                              HTML / text reports
```

**End-to-end pipeline**

1. **Harvest (`vendemmia`)** — connect to each device over telnet, run diagnostic
   commands, follow neighbour discovery to walk the network, and store the raw
   output as the `vinaccia` archive.
2. **Distil (`distillatore`)** — parse each file with regular expressions, extract
   the key parameters, cluster the configurations by structural similarity, build
   the connectivity graph, and generate the `grappa` HTML/text reports.
3. **Visualise (`Topology`)** — compute centrality over the network graph and
   render an SVG diagram with a ranked device list.

For the full module-by-module breakdown, the clustering mechanism, and the class
relationships, see [docs/architecture.html](docs/architecture.html).

---

## Modules in this repository

| Folder | Module | Role |
|--------|--------|------|
| `vendemmia/` | **vendemmiatore** | Telnet harvesting + neighbour discovery (produces the `vinaccia`) |
| `distillatore/`, `distillatore3/` | **distillatore** | Regex parsing, cluster analysis, HTML/text report generation (the `grappa`) |
| `Topology Public/`, `Topology_1.0/`, `Topology_1.1/`, `Topology_2.0/` | **Topology** | Network centrality + SVG visualisation (successive versions) |
| `xmlTree/` | xmlTree | Collapsible HTML tree menus for navigating the analysis output |
| `tester/` | Tester | Support / testing project |
| `Telnet/`, `Dragon Server/` | third-party | Networking libraries and support material used by the tools |

---

## Technology

- **Language:** Java (Swing/AWT desktop UIs, `Runnable`-based concurrency)
- **Connectivity:** telnet via the Apache ORO networking library
- **Parsing:** `java.util.regex` (Pattern/Matcher), used extensively for
  multi-line configuration blocks
- **Data & output:** XML/DOM, HTML report tables, SVG diagrams
- **Build/IDE of the era:** Borland JBuilder projects (`.jpr` / `.jpx`), with a
  few Borland JBCL layout helpers in the UI

---

## A note on the code

This is genuinely old code, kept as a historical archive. It reflects the tools,
libraries, and conventions of its time, and the comments are largely in Italian.
It is published here as a record of an early, and rather formative, engineering
project rather than as a maintained product.

---

<a id="italiano"></a>

# Italiano

Una piccola suite di strumenti Java, scritta all'inizio degli anni 2000 (i file
sorgente riportano un copyright del 2002), realizzata per **automatizzare la
valutazione di qualità di grandi reti aziendali**. Il nome del progetto e dei
suoi moduli prende in prestito la terminologia della vinificazione, perché la
pipeline ricalca il modo in cui l'uva diventa un distillato finito: si raccoglie
la materia prima, la si distilla e si ottiene qualcosa di raffinato, pronto per
essere valutato da un esperto.

Una panoramica interattiva e bilingue dell'architettura è disponibile in
[docs/architecture.html](docs/architecture.html) (aprilo in un browser).

---

## La storia

All'epoca lavoravo come ingegnere di rete per una società di system integration
e consulenza. Insieme a un gruppo di ingegneri, fui assegnato a un grande
progetto per un importante operatore di telecomunicazioni. Diverse aziende erano
state coinvolte per lo stesso lavoro, così alcuni team si trovavano di fatto in
competizione tra loro, misurati su velocità e qualità. Chi avesse ottenuto i
risultati migliori avrebbe mantenuto il contratto.

Il compito era chiaro e ripetitivo: stare davanti a un terminale tutto il giorno
e, un dispositivo alla volta, connettersi a ciascun router della rete di un
cliente tramite un'interfaccia testuale, scaricarne la configurazione, eseguire
una sequenza fissa di comandi diagnostici, leggere l'output e individuare ogni
errore di configurazione. Il risultato finale era un report che elencava i
difetti trovati nella rete. Il lavoro era del tutto individuale e non richiedeva
alcuna interazione umana.

In molti pensarono di automatizzarlo, ma quasi nessuno lo riteneva possibile: le
configurazioni erano testo amorfo, scritto in un linguaggio di dispositivo
ricco e pieno di eccezioni. Due file di configurazione erano raramente identici:
indirizzamenti IP diversi, versioni di firmware diverse e il fatto che lo stesso
comportamento potesse essere espresso in modi leggermente differenti.

### Una decisione coraggiosa

Non avevo esperienza di sviluppo software, ma ero convinto che la parte noiosa
del lavoro potesse essere automatizzata, lasciando all'ingegnere il compito di
aggiungere il giudizio di alto livello che davvero contava. La mia idea era uno
strumento capace di produrre un **report semilavorato**: un solido punto di
partenza che un ingegnere avrebbe potuto completare con analisi e contesto.

Un amico programmatore esperto mi consigliò Java. Lo studiai, presentai il piano
al management e mi impegnai a consegnare uno strumento funzionante in sei
settimane. Chiesi di svilupparlo al posto del lavoro manuale e di avere un
piccolo team tra le aziende coinvolte. Lo sponsor lato cliente e il mio
management sostennero l'idea, anche se solo una delle altre aziende ci credette
abbastanza da mettere a disposizione una persona.

### Durante lo sviluppo

La primissima cosa che feci fu ordinare un libro su Java — prima ancora di avere
il via libera formale — perché all'epoca la spedizione internazionale verso
l'Italia richiedeva circa una settimana.

Dopo una serie di incontri con colleghi e cliente per definire l'architettura,
lavorai alla soluzione giorno e notte. L'impegno fu diviso: un collega prese in
carico il **modulo di connessione**, mentre io costruivo il **modulo di analisi
e output**. In tre settimane la parte di analisi elaborava già un piccolo
campione di file di configurazione, e consegnai la prima release al team. Era
piena di bug, limitata e grezza, ma dimostrava che il concetto funzionava, e mi
fu permesso di proseguire.

Il modulo di connessione, purtroppo, non funzionava, e il mio collega si tirò
indietro dalla sua parte. Presi in carico anche il suo componente. Una release
utilizzabile, modulo di connessione incluso, fu pronta in circa sette settimane:
solo poco più tardi di quanto promesso. Da lì il team iniziò a usarla ogni
giorno, segnalando bug e, sempre di più, contribuendo a migliorarla.

---

## La soluzione

D'accordo con il team, i componenti furono battezzati con termini della
vinificazione.

- **`vendemmiatore`** — il modulo di raccolta. Si connette alla rete del cliente
  tramite l'interfaccia testuale, parte da un singolo dispositivo, scopre i
  vicini e percorre l'intera rete, scaricando ogni file di configurazione e di
  diagnostica. L'archivio di file grezzi risultante fu chiamato **`vinaccia`**
  (la materia prima dell'uva, pronta per essere lavorata).

- **`distillatore`** — il modulo di analisi e output. Prende la `vinaccia` in
  ingresso e produce una serie di documenti riassuntivi in HTML, chiamati
  collettivamente **`grappa`**. Costruito attorno al framework delle **espressioni
  regolari** (lo stesso tipo di pattern matching usato per elaborare le sequenze
  del DNA), il distillatore trasforma testo disordinato in report strutturati e
  leggibili: un prodotto semilavorato che gli ingegneri potevano rifinire fino
  alla valutazione completa.

- **`Topology`** — un visualizzatore complementare. Dalla rete scoperta calcola
  la centralità dei nodi (distanze di cammino minimo) e produce un diagramma di
  rete in **SVG**, oltre a un elenco HTML dei dispositivi ordinati per
  centralità.

La parte più preziosa della `grappa` era una **cluster analysis** delle
configurazioni dei dispositivi. Il distillatore confrontava le configurazioni di
tutti i dispositivi e le raggruppava in cluster di configurazioni simili. La
similarità non era un banale confronto tra stringhe di testo: era una funzione
strutturata, basata su espressioni regolari, che ignorava le parti che
legittimamente differiscono (come gli specifici indirizzi IP) e si concentrava
sulla forma strutturale della configurazione — il tutto restando nascosto
all'ingegnere. Conoscendo i cluster, l'ingegnere non doveva più esaminare ogni
file singolarmente: poteva concentrarsi sui pochi cluster distinti emersi,
rendendo molto più facile individuare gli errori.

---

## Architettura in breve

```
  RACCOLTA                   DISTILLAZIONE                  VISUALIZZAZIONE
  ┌───────────────┐          ┌──────────────────┐          ┌───────────────┐
  │ vendemmiatore │  ─────▶  │   distillatore   │  ─────▶  │   Topology    │
  │  (telnet,     │ vinaccia │  (parsing regex, │  grafo   │  (centralità, │
  │   discovery)  │  (file)  │   clustering)    │          │   output SVG) │
  └───────────────┘          └──────────────────┘          └───────────────┘
                                     │ grappa
                                     ▼
                              report HTML / testo
```

**Pipeline end-to-end**

1. **Raccolta (`vendemmia`)** — connessione a ciascun dispositivo via telnet,
   esecuzione dei comandi diagnostici, scoperta dei vicini per percorrere la rete
   e salvataggio dell'output grezzo come archivio `vinaccia`.
2. **Distillazione (`distillatore`)** — parsing di ogni file con espressioni
   regolari, estrazione dei parametri chiave, clustering delle configurazioni per
   similarità strutturale, costruzione del grafo di connettività e generazione
   dei report `grappa` in HTML/testo.
3. **Visualizzazione (`Topology`)** — calcolo della centralità sul grafo di rete e
   rendering di un diagramma SVG con l'elenco ordinato dei dispositivi.

Per la scomposizione completa modulo per modulo, il meccanismo di clustering e le
relazioni tra le classi, vedi [docs/architecture.html](docs/architecture.html).

---

## Moduli in questo repository

| Cartella | Modulo | Ruolo |
|----------|--------|-------|
| `vendemmia/` | **vendemmiatore** | Raccolta via telnet + scoperta dei vicini (produce la `vinaccia`) |
| `distillatore/`, `distillatore3/` | **distillatore** | Parsing regex, cluster analysis, generazione report HTML/testo (la `grappa`) |
| `Topology Public/`, `Topology_1.0/`, `Topology_1.1/`, `Topology_2.0/` | **Topology** | Centralità di rete + visualizzazione SVG (versioni successive) |
| `xmlTree/` | xmlTree | Menu ad albero HTML collassabili per navigare l'output dell'analisi |
| `tester/` | Tester | Progetto di supporto / test |
| `Telnet/`, `Dragon Server/` | terze parti | Librerie di rete e materiale di supporto usati dagli strumenti |

---

## Tecnologia

- **Linguaggio:** Java (interfacce desktop Swing/AWT, concorrenza basata su
  `Runnable`)
- **Connettività:** telnet tramite la libreria di rete Apache ORO
- **Parsing:** `java.util.regex` (Pattern/Matcher), usato estensivamente per i
  blocchi di configurazione multi-riga
- **Dati e output:** XML/DOM, tabelle di report HTML, diagrammi SVG
- **Build/IDE dell'epoca:** progetti Borland JBuilder (`.jpr` / `.jpx`), con
  alcuni helper di layout Borland JBCL nell'interfaccia

---

## Una nota sul codice

Questo è codice davvero datato, conservato come archivio storico. Riflette
strumenti, librerie e convenzioni della sua epoca, e i commenti sono in larga
parte in italiano. È pubblicato qui come testimonianza di un progetto ingegneristico
iniziale, e piuttosto formativo, più che come prodotto mantenuto.
