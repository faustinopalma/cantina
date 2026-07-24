<!--
  🇮🇹 Per la versione in ITALIANO, vai alla sezione "Italiano" più in basso:
     https://github.com/faustinopalma/cantina#italiano
  The English version comes first, the Italian version follows.
-->

# Cantina — Network Configuration Assessment Toolchain

A small suite of Java tools, written in 2002, built to **automate the quality
assessment of large corporate networks**. The project name and its modules borrow
terminology from
winemaking, because the pipeline mirrors the way grapes become a finished
spirit: you harvest the raw material, you distil it, and you obtain something
refined and ready to be tasted by an expert.

An interactive, bilingual overview of the architecture is available in
[docs/architecture.html](docs/architecture.html) (open it in a browser).

---

## The story

Back then I was a young network engineer at a system-integration and consulting
company, part of a team assigned to a large project for a major telecommunications
operator. Several companies had been brought in for the same work, so a handful of
teams ended up doing very similar things in parallel, measured on speed and
quality. There was a friendly, if real, sense of competition in the air.

The task was clear and, honestly, quite tedious: sit at a terminal and, one device
at a time, connect to each router in a client network through a text-based
interface, download its configuration, run a fixed sequence of diagnostic commands,
read the output, and look for configuration mistakes. The deliverable was a report
listing the defects found across the network — careful, patient, and almost entirely
manual work.

The idea of automating it came up more than once, but most of us (myself included at
first) assumed it wasn't really practical: the configurations were plain, amorphous
text in a rich device language full of exceptions. Two files were rarely identical —
different IP addressing, different firmware versions, and the same behaviour often
written in slightly different ways.

### Taking a chance

I had no real software experience, but I kept wondering whether at least the tedious
part could be handled by a program, leaving the engineer free to add the judgement
that actually mattered. The goal wasn't a magic button — just a tool that produced a
**semi-finished report**, a decent starting point someone could refine with insight
and context.

A friend who actually knew how to program suggested I learn Java. I gave it a try,
sketched out a plan, and asked management whether I could build the tool instead of
doing the manual job, ideally with a small cross-company team. Looking back, it was a
bit of a leap of faith on everyone's part: the client sponsor and my managers decided
to trust the idea, even though only one of the other companies was willing to lend a
hand.

### Building it

The first thing I did was order a Java book — before I even had the formal go-ahead —
since international shipping to Italy took about a week back then, and I didn't want to
lose the time.

After some meetings with colleagues and the client to shape the design, I spent a lot
of long evenings on it, more out of enthusiasm than heroism. We split the work: a
colleague took the **connection module**, and I worked on the **analysis and output
module**. Within a few weeks the analysis part could already chew through a small
sample of configuration files, so I shared an early release with the team. It was
buggy, limited, and rough, but it was enough to show the idea could work — and that
was enough to keep going.

The connection module turned out to be the harder problem, and in the end my colleague
decided to step away from it. I picked up that part too, and with plenty of trial and
error a usable release — connection module included — came together in about seven
weeks, a little later than I'd hoped. What made the real difference from there was the
team: they used it every day, reported bugs, and gradually helped turn it into
something genuinely useful.

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

Una piccola suite di strumenti Java, scritta nel 2002, realizzata per
**automatizzare la valutazione di qualità di grandi reti aziendali**. Il nome del
progetto e dei
suoi moduli prende in prestito la terminologia della vinificazione, perché la
pipeline ricalca il modo in cui l'uva diventa un distillato finito: si raccoglie
la materia prima, la si distilla e si ottiene qualcosa di raffinato, pronto per
essere valutato da un esperto.

Una panoramica interattiva e bilingue dell'architettura è disponibile in
[docs/architecture.html](docs/architecture.html) (aprilo in un browser).

---

## La storia

All'epoca ero un giovane ingegnere di rete in una società di system integration e
consulenza, parte di un team assegnato a un grande progetto per un importante
operatore di telecomunicazioni. Diverse aziende erano state coinvolte per lo stesso
lavoro, così alcuni team finivano per fare cose molto simili in parallelo, misurati
su velocità e qualità. Nell'aria c'era una competizione, amichevole ma reale.

Il compito era chiaro e, va detto, piuttosto noioso: sedersi a un terminale e, un
dispositivo alla volta, connettersi a ciascun router della rete di un cliente
tramite un'interfaccia testuale, scaricarne la configurazione, eseguire una sequenza
fissa di comandi diagnostici, leggere l'output e cercare gli errori di
configurazione. Il risultato era un report con i difetti trovati nella rete — un
lavoro attento, paziente e quasi del tutto manuale.

L'idea di automatizzarlo era venuta più di una volta, ma quasi tutti (all'inizio me
compreso) la ritenevano poco praticabile: le configurazioni erano testo amorfo, in un
linguaggio di dispositivo ricco e pieno di eccezioni. Due file erano raramente
identici: indirizzamenti IP diversi, versioni di firmware diverse e lo stesso
comportamento spesso scritto in modi leggermente differenti.

### Un salto nel buio

Non avevo una vera esperienza di sviluppo, ma continuavo a chiedermi se almeno la
parte noiosa potesse essere gestita da un programma, lasciando all'ingegnere il
giudizio che davvero contava. L'obiettivo non era un pulsante magico: solo uno
strumento capace di produrre un **report semilavorato**, un punto di partenza decente
da rifinire con analisi e contesto.

Un amico che sapeva davvero programmare mi suggerì di imparare Java. Ci provai,
buttai giù un piano e chiesi al management se potevo costruire lo strumento al posto
del lavoro manuale, possibilmente con un piccolo team tra le aziende coinvolte. Col
senno di poi fu un piccolo atto di fiducia da parte di tutti: lo sponsor lato cliente
e i miei responsabili scelsero di credere nell'idea, anche se solo una delle altre
aziende fu disposta a dare una mano.

### La realizzazione

La prima cosa che feci fu ordinare un libro su Java — prima ancora del via libera
formale — perché all'epoca la spedizione dall'estero verso l'Italia richiedeva circa
una settimana e non volevo perdere tempo.

Dopo qualche incontro con colleghi e cliente per definire l'impostazione, ci passai
molte serate lunghe, più per entusiasmo che per eroismo. Ci dividemmo il lavoro: un
collega prese il **modulo di connessione** e io mi occupai del **modulo di analisi e
output**. In poche settimane la parte di analisi riusciva già a elaborare un piccolo
campione di file, così condivisi una prima release con il team. Era piena di bug,
limitata e grezza, ma bastava a mostrare che l'idea poteva funzionare — e tanto
bastava per andare avanti.

Il modulo di connessione si rivelò il problema più difficile e alla fine il collega
decise di lasciarlo. Presi in carico anche quella parte e, con parecchi tentativi ed
errori, una release utilizzabile — modulo di connessione incluso — prese forma in
circa sette settimane, un po' più tardi di quanto speravo. A fare la differenza da lì
in poi fu il team: la usavano ogni giorno, segnalavano bug e, poco alla volta,
aiutarono a trasformarla in qualcosa di davvero utile.

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
