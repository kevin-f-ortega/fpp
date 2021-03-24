package fpp.compiler.analysis

import fpp.compiler.ast._
import fpp.compiler.util._

/** A class for resolving connection patterns */
sealed trait PatternResolver {

  /** The type of a source */
  type Source

  /** The type of the target */
  type Target

  /** The enclosing analysis */
  val a: Analysis

  /** The connection pattern to resolve */
  val pattern: ConnectionPattern

  /** The component instances in scope */
  val instances: List[ComponentInstance]

  /** Resolve the source */
  def resolveSource: Result.Result[Source]

  /** Resolve a target */
  def resolveTarget(
    loc: Location, /** The location where the target was declared */
    target: ComponentInstance /** The target */
  ): Result.Result[Target]

  /** Generate the connections for a source and target */
  def getConnectionsForTarget(source: Source, target: Target): List[Connection]

  /** Resolve the pattern to a list of connections */
  final def resolve: Result.Result[List[Connection]] =
    for {
      source <- resolveSource
      targets <- resolveTargets
    }
    yield targets.flatMap(getConnectionsForTarget(source, _))

  private def resolveTargets: Result.Result[List[Target]] =
    pattern.targets.size match {
      case 0 => resolveImplicitTargets
      case _ => resolveExplicitTargets
    }

  private def resolveImplicitTargets: Result.Result[List[Target]] = {
    val loc = Locations.get(pattern.aNode._2.id)
    val targets = instances.map(target => resolveTarget(loc, target)).
      filter(_.isRight).map(Result.expectRight)
    Right(targets)
  }

  private def resolveExplicitTargets: Result.Result[List[Target]] =
    Result.map(
      pattern.targets.toList,
      target => resolveTarget(Locations.get(target.aNode._2.id), target)
    )

}

object PatternResolver {

  /** Resolve a pattern */
  def resolve(
    a: Analysis,
    pattern: ConnectionPattern,
    instances: List[ComponentInstance]
  ): Result.Result[List[Connection]] = {
    import Ast.SpecConnectionGraph._
    val resolverOpt: Option[PatternResolver] = pattern.pattern.kind match {
      case Pattern.Command => None
      case Pattern.Event => None
      case Pattern.Health => None
      case Pattern.Telemetry => None
      case Pattern.Time => None
    }
    resolverOpt match {
      case Some(resolver) => resolver.resolve
      case None => Right(Nil)
    }
  }

  final case class Time(
    a: Analysis,
    pattern: ConnectionPattern,
    instances: List[ComponentInstance]
  ) extends PatternResolver {

    type Source = PortInstanceIdentifier

    type Target = PortInstanceIdentifier

    override def resolveSource: Result.Result[Source] = {
      def isTimeGetIn(pi: PortInstance): Boolean = {
        false
      }

      ???
    }

    override def resolveTarget(
      loc: Location,
      target: ComponentInstance
    ): Result.Result[Target] = ???

    override def getConnectionsForTarget(
      source: Source,
      target: Target
    ): List[Connection] = ???

  }


}