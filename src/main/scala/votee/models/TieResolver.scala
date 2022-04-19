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