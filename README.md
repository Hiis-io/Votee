![Repository stars](https://img.shields.io/github/stars/hiis-io/votee?style=flat)
![GitHub issues by-label](https://img.shields.io/github/issues/hiis-io/votee/good%20first%20issue?label=Good%20First%20Issues)
![GitHub closed pull requests](https://img.shields.io/github/issues-pr-closed/hiis-io/votee?color=violet&label=Pull%20Requests)
![Scala Version](https://img.shields.io/badge/Scala-3.2.0-red)
![SBT Version](https://img.shields.io/badge/SBT-1.6.2-blueviolet)
![Scala CI](https://github.com/hiis-io/votee/actions/workflows/scala.yml/badge.svg)
[![codecov](https://codecov.io/gh/Hiis-io/Votee/branch/master/graph/badge.svg?token=CZ0640Q6OG)](https://codecov.io/gh/Hiis-io/Votee)

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
  github.com/hiis-io/zio-auth-server
## Core Concepts
The library is meant to be highly extensible by the user. It was designed to be used with your own components such as your own definition of a Ballot or Candidate. The library consist of the following core concepts.
1. `Election`
2. `Ballot`
3. `Candidate`
4. `TieResolver`
5. `Winner`

Each of these components are made to be extensible, however the library is provided with a default implementation of each component. This enables users to use the library or test it without implementing their own components.

## Usage
Currently, the library isn't yet published to maven hence developers will have to use the JAR provided in the output folder.
Follow the steps bellow to add votee to your scala application.

1. In the root directory of your scala application create a directory called `lib`
2. Copy the JAR file(`votee-x.y.z.jar`) from [`output`](./output) to your `lib` directory.

3. Test the library with the following code snippet
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
   1. Candidate: If the current implementation of the Candidate doesn't suit your needs you can create yours by extending the candidate trait 
      ```scala
         import votee.models.Candidate
         final case class MyCandidate(override val id: String, name: String, ...) extends Candidate(id)
      ```
   2. Ballot: You are advised not to build your own Ballot and rather used the builtin implementation which is `PreferentialBallot`. You can however extend the current implementation of Ballot if you really have to. Below is how the `PreferentialBallot` is implemented.
      ```scala
        import votee.models.{Ballot, Candidate}
        import spire.math.Rational
      
        final case class PreferentialBallot[+C <: Candidate](override val id: Int, override val weight: Rational = Rational(1, 1), override val preferences: Seq[C])
            extends Ballot[C, PreferentialBallot](id, weight, preferences) :
      
            override def --[CC >: C <: Candidate](candidates: Seq[CC]): PreferentialBallot[CC] = PreferentialBallot(id, weight, preferences.filterNot(candidates.contains(_)))
      
            override def ++[CC >: C <: Candidate](candidates: Seq[CC]): PreferentialBallot[CC] = PreferentialBallot(id, weight, candidates ++ preferences)
      
        end PreferentialBallot
      ```
   3. TieResolver: There are 3 implementations of TieResolver which you can simply import from `Election.TieResolvers`. TieResolvers describe how the algorithms should resolve ties, which is absolutely necessary when running the election. Below are the different TieResolvers already implemented in the library.
        1. DoNothingTieResolver (Default)
        2. RandomTieResolver
        3. ReverseTieResolver
      
      If for some reason you would like to resolve ties differently from what is proposed above, you need to implement the `TieResolver trait`. Below is the contract
      ```scala
        import spire.math.Rational
        import votee.models.Candidate
      
        /**
         * TieResolver describes the contract on how to resolve candidates with the same scores.
         * 
         * Given a list of candidates with the same score return a new list with a desired ordering.
         *
         * The resolve function must satisfy the following conditions:
         *
         * 1. Total number of elements in the original list should be same as that of returned list
         * 
         * 2. All elements in the original list should be found in the returned list
         * @tparam C
         */
         trait TieResolver[C <: Candidate]:
           def resolve(candidateScores: List[(C, Rational)]): List[(C, Rational)]
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



