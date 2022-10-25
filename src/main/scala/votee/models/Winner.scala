package votee.models

import spire.math.Rational


/**
 * Created by Abanda Ludovic on 29/03/2022.
 */

/**
 * Election winner
 * @param candidate winning candidate
 * @param score candidates total score
 *
 * @tparam C candidate type
 */
final case class Winner[C <: Candidate](candidate: C, score: Rational)

object Winner:
  def apply[C <: Candidate](winner: (C, Rational)): Winner[C] = Winner(winner._1, winner._2)