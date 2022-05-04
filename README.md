![Repository stars](https://img.shields.io/github/stars/hiis-io/votee?style=flat)
![GitHub issues by-label](https://img.shields.io/github/issues/hiis-io/votee/good%20first%20issue?label=Good%20First%20Issues)
![GitHub closed pull requests](https://img.shields.io/github/issues-pr-closed/hiis-io/votee?color=violet&label=Pull%20Requests)
![Scala Version](https://img.shields.io/badge/Scala-3.1.0-red)
![SBT Version](https://img.shields.io/badge/SBT-1.6.2-blueviolet)

## Intro
Votee is a library of data structures and algorithms for counting votes in Elections. This library is inspired from the Agora vote counting library. It is a lightweight vote counting library implemented in scala 3.

Currently, the following vote counting algorithms are implemented:

* `Approval`
* `Baldwin`
* `Borda`
* `Contingent`
* `Coomb`
* `Exhausive Ballot`
* `Majority`
* `Super Majority`
* `Veto`

## Core Concepts
The library is meant to be highly extensible by the user. It was designed to be used with your own components such as your own definition of a Ballot or Candidate. The library consist of the following core concepts.
1. `Election`
2. `Ballot`
3. `Candidate`
4. `TieResolver`
5. `Winner`

Each of these components are made to be extensible, however the library is provided with a default implementation of each component. This enables users to use the library or test it without implementing their own components.

## Usage
Since this is a `Scala 3` project and so far no Fat JAR packager library such as `sbt-assembly` currently supports `scala 3 projects`, developers will have to use the JAR provided in the output folder.
Follow the steps bellow to add votee to your scala application.

1. In the root directory of your scala application create a directory called `lib`
2. Copy the JAR file(`votee-x.y.z.jar`) from [`output`](./output) to your `lib` directory.
3. Since the dependencies are not provided with the jar file, add the following to your build.sbt
    ```
    libraryDependencies ++= Seq(
      "org.scalanlp" %% "breeze" % "2.0.1-RC1",
      "com.typesafe.play" %% "play-json" % "2.10.0-RC6"
    )
   ```
4. Test the application with the following code snippet
   ```scala
      import spire.math.Rational
      import votee.algorithms.Majority
      import votee.models.{Election, PreferentialBallot, PreferentialCandidate, TieResolver}
   
      import scala.util.Random
   
   
   
      object Main extends App {
        val candidates = List(PreferentialCandidate("a", "A"), PreferentialCandidate("b", "B"))
        val ballots = List(
          PreferentialBallot(1, Rational(1), Random.shuffle(candidates)),
          PreferentialBallot(2, Rational(1), Random.shuffle(candidates)),
          PreferentialBallot(3, Rational(1), Random.shuffle(candidates)),
          PreferentialBallot(4, Rational(1), Random.shuffle(candidates)),
          PreferentialBallot(5, Rational(1), Random.shuffle(candidates)),
          PreferentialBallot(6, Rational(1), Random.shuffle(candidates)),
          PreferentialBallot(7, Rational(1), Random.shuffle(candidates)),
          PreferentialBallot(9, Rational(1), Random.shuffle(candidates)),
          PreferentialBallot(10, Rational(1), Random.shuffle(candidates)))
   
        //given tieResolver: TieResolver[PreferentialCandidate] = Election.TieResolvers.randomTieResolver[PreferentialCandidate]
   
        val winner = Majority.run(ballots, candidates, 1)
        println(s"Winner is: ${winner}")
      }
   ```
### Extending the core components
   1. Candidate: If the current implementation of the Candidate doesn't suit your needs you can create your by extending the candidate class 
      ```scala
         import votee.models.Candidate
         final case class MyCandidate(override val id: String, name: String, ...) extends Candidate(id)
      ```
   2. Ballot: You are advised not to build your own Ballot and rather used the builtin implementation which is `PreferentialBallot`. You can however extend the current implementation of Ballot if you really have to. Below is how the `PreferentialBallot` is implemented.
      ```scala
        import votee.models.{Ballot, Candidate}
        import spire.math.Rational
      
        final case class PreferentialBallot[C <: Candidate](override val id: Int, override val weight: Rational = Rational(1, 1), override val preferences: List[C])
        extends Ballot[C](id, weight, preferences):
          override type B = PreferentialBallot[C]
          override def excludeCandidates(candidates: List[C]): PreferentialBallot[C] = PreferentialBallot(id, weight, preferences.filterNot(candidates.contains(_)))
          override def includeCandidates(candidates: List[C]): PreferentialBallot[C] = PreferentialBallot(id, weight, preferences ++ candidates)
        end PreferentialBallot
      ```
## Development


#TODO

* [`InstantExhaustiveDropOff`](https://en.wikipedia.org/wiki/Exhaustive_ballot#Notes)
* [`InstantRunoff2Round`](https://en.wikipedia.org/wiki/Two-round_system)
* `Kemeny-Young`
* [`MinimaxCondorcet`](https://en.wikipedia.org/wiki/Minimax_Condorcet)
* [`Nanson`](https://en.wikipedia.org/wiki/Nanson%27s_method)
* [`Oklahoma`](https://en.wikipedia.org/wiki/Oklahoma_primary_electoral_system)
* [`PAV - Proportional Approval Voting`](https://en.wikipedia.org/wiki/Proportional_approval_voting)
* [`PreferentialBlockVoting`](https://en.wikipedia.org/wiki/Preferential_block_voting)
* [`RandomBallot`](https://en.wikipedia.org/wiki/Random_ballot)
* [`SAV - Satisfaction Approval Voting`](https://en.wikipedia.org/wiki/Satisfaction_approval_voting)



