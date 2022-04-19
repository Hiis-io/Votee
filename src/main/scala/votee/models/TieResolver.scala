package votee.models

import votee.utils.Rational

/**
 * Created by Abanda Ludovic on 19/04/2022.
 */

/**
 * TieResolver describes the contract on how to resolve ties in an election result.
 * This is particularly helpful when some candidates have the same scores and th number of vacancies is less than the total leading candidates
 * @tparam C
 */
trait TieResolver[C <: Candidate]:
  def resolve(candidateScores: List[(C, Rational)], vacancies: Int): List[(C, Rational)]

/**
 * A naive Tie Resolver that takes the first N leading candidates.
 * This doesn't take into consideration that some of the other candidates left out may have the same scores as the leading ones.
 * @tparam C
 */
trait TakeNTieResolver[C <: Candidate] extends TieResolver[C]:
  override def resolve(candidateScores: List[(C, Rational)], vacancies: Int): List[(C, Rational)] = candidateScores.sortWith(_._2 > _._2).take(vacancies)
