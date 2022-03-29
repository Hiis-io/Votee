package votee.models

import votee.utils.Rational


/**
 * Created by Abanda Ludovic on 29/03/2022.
 */

final case class Winner[C <: Candidate](candidate: C, weight: Rational)

object Winner {
  def apply[C <: Candidate](winner: (C, Rational)): Winner[C] = Winner(winner._1, winner._2)
}