package io.hiis.votee.algorithms

import io.hiis.votee.models.{
  Ballot,
  Candidate,
  Election,
  PreferentialElection,
  TieResolver,
  Winner
}
import spire.math.Rational

import scala.annotation.tailrec

import scala.collection.mutable

/**
 * Created by Abanda Ludovic on 01/04/2022. Algorithm:
 * https://en.wikipedia.org/wiki/Coombs%27_method Note: This voting method requires voters to rank
 * all the candidates
 */

sealed trait Coomb[C <: Candidate, B[+CC >: C <: Candidate] <: Ballot[CC, B]]
    extends PreferentialElection[C, B]:
  @tailrec
  final override def run(ballots: Seq[B[C]], candidates: Seq[C], vacancies: Int)(using
      tieResolver: TieResolver[C] = DEFAULT_TIE_RESOLVER
  ): Seq[Winner[C]] =
    if candidates.isEmpty then Seq.empty
    else
      val candidateScoreMap: mutable.HashMap[C, Rational] =
        mutable.HashMap.empty ++ countFirstVotes(ballots, candidates)
      if candidateScoreMap.toList
          .filter(_._2 > MAJORITY_THRESHOLD * Rational(ballots.length))
          .take(1)
          .nonEmpty
      then
        resolveTies(
          candidateScoreMap.toList
            .filter(w => w._2 > MAJORITY_THRESHOLD * Rational(ballots.length))
            .sortWith(_._2 > _._2)
        ).map(Winner(_)).take(1)
      else
        val lastCandidateScoreMap: mutable.HashMap[C, Rational] =
          mutable.HashMap.empty ++ countLastVotes(ballots, candidates)
        val highestRankingLast: C = resolveTies(
          lastCandidateScoreMap.toList.sortWith(_._2 > _._2)
        ).head._1
        run(ballots, candidates.filterNot(_ == highestRankingLast), vacancies)
  end run
end Coomb

case object Coomb:
  def run[C <: Candidate, B[+CC >: C <: Candidate] <: Ballot[CC, B]](
      ballots: Seq[B[C]],
      candidates: Seq[C],
      vacancies: Int
  )(using
      tieResolver: TieResolver[C] = Election.TieResolvers.doNothingTieResolver[C]
  ): Seq[Winner[C]] =
    new Coomb[C, B] {}.run(ballots, candidates, vacancies)
end Coomb
