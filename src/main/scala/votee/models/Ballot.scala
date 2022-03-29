package votee.models

import votee.utils.Rational

/**
 * Created by Abanda Ludovic on 29/03/2022.
 */

abstract class Ballot[+C](val id: Int, val weight: Rational)

class PreferenceBallot[C <: Candidate](override val id: Int, override val weight: Rational, val preferences: List[C])
  extends Ballot[C](id, weight)