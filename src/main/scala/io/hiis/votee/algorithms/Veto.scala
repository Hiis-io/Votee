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

import scala.collection.mutable

/** Created by Abanda Ludovic on 29/03/2022. */

sealed trait Veto[C <: Candidate, B[+CC >: C <: Candidate] <: Ballot[CC, B]]
    extends PreferentialElection[C, B]:

  final override def run(ballots: Seq[B[C]], candidates: Seq[C], vacancies: Int)(using
      tieResolver: TieResolver[C] = DEFAULT_TIE_RESOLVER
  ): Seq[Winner[C]] =
    val candidateScoreMap = new mutable.HashMap[C, Rational]
    ballots.foreach(ballot =>
      ballot.preferences.foreach(preference =>
        if !(preference == ballot.preferences.last && ballot.preferences.length > 1) then
          // Automatically assign a score of 1 to all candidates in the ballot except the last candidate as described by the veto rule
          candidateScoreMap(preference) =
            candidateScoreMap.getOrElse(preference, Rational(0, 1)) + Rational(1)
      )
    )
    candidateScoreMap.toList.sortWith(_._2 > _._2).take(vacancies).map(Winner(_))
  end run
end Veto

case object Veto:
  def run[C <: Candidate, B[+CC >: C <: Candidate] <: Ballot[CC, B]](
      ballots: Seq[B[C]],
      candidates: Seq[C],
      vacancies: Int
  )(using
      tieResolver: TieResolver[C] = Election.TieResolvers.doNothingTieResolver[C]
  ): Seq[Winner[C]] =
    new Veto[C, B] {}.run(ballots, candidates, vacancies)
end Veto
