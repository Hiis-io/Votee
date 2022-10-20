package votee.models

import spire.math.Rational

import scala.util.{Failure, Success, Try}

/**
 * Created by Abanda Ludovic on 29/03/2022.
 */

trait Ballot[+C <: Candidate, +T[+CC >: C <: Candidate] <: Ballot[CC, T]](val id: Int, val weight: Rational, val preferences: Seq[C]):
  def --[CC >: C <: Candidate](candidates: Seq[CC]): T[CC]

  def ++[CC >: C <: Candidate](candidates: Seq[CC]): T[CC]
end Ballot

final case class PreferentialBallot[+C <: Candidate](override val id: Int, override val weight: Rational = Rational(1, 1), override val preferences: Seq[C])
  extends Ballot[C, PreferentialBallot](id, weight, preferences) :

  override def --[CC >: C <: Candidate](candidates: Seq[CC]): PreferentialBallot[CC] = PreferentialBallot(id, weight, preferences.filterNot(candidates.contains(_)))

  override def ++[CC >: C <: Candidate](candidates: Seq[CC]): PreferentialBallot[CC] = PreferentialBallot(id, weight, candidates ++ preferences)

end PreferentialBallot
