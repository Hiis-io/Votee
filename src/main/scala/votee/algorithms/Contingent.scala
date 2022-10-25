package votee.algorithms

import spire.math.Rational
import votee.models.*

import scala.collection.mutable

/**
 * Created by Abanda Ludovic on 31/03/2022. Algorithm as described at
 * https://en.wikipedia.org/wiki/Contingent_vote
 */

sealed trait Contingent[C <: Candidate, B[+CC >: C <: Candidate] <: Ballot[CC, B]]
    extends PreferentialElection[C, B]:
  final override def run(ballots: Seq[B[C]], candidates: Seq[C], vacancies: Int)(using
      tieResolver: TieResolver[C] = DEFAULT_TIE_RESOLVER
  ): Seq[Winner[C]] =
    val candidateScoreMap: mutable.HashMap[C, Rational] =
      mutable.HashMap.empty ++ countFirstVotes(ballots, candidates)

    val sortedTotals: Seq[(C, Rational)] = resolveTies(
      candidateScoreMap.toList.sortWith(_._2 > _._2)
    )
    if sortedTotals.head._2 > MAJORITY_THRESHOLD then List(Winner(sortedTotals.head))
    else
      val firstRoundCandidates: Seq[C] = sortedTotals.take(2).map(_._1)
      ballots.filterNot(ballot => firstRoundCandidates.contains(ballot.preferences.head)).foreach {
        ballot =>
          ballot.preferences.find(firstRoundCandidates.contains(_)) match
            case Some(candidate) =>
              candidateScoreMap(candidate) =
                ballot.weight + candidateScoreMap.getOrElse(candidate, Rational(0))
            case _ =>
      }
      List(resolveTies(candidateScoreMap.toList.sortWith(_._2 > _._2)).map(Winner(_)).head)
  end run
end Contingent

case object Contingent:
  def run[C <: Candidate, B[+CC >: C <: Candidate] <: Ballot[CC, B]](
      ballots: Seq[B[C]],
      candidates: Seq[C],
      vacancies: Int
  )(using
      tieResolver: TieResolver[C] = Election.TieResolvers.doNothingTieResolver[C]
  ): Seq[Winner[C]] =
    new Contingent[C, B] {}.run(ballots, candidates, vacancies)
end Contingent
