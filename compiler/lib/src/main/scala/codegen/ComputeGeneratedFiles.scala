package fpp.compiler.codegen

import fpp.compiler.analysis._
import fpp.compiler.ast._
import fpp.compiler.util._

/** Computes the names of generated files */
object ComputeGeneratedFiles {

  def getFiles(tul: List[Ast.TransUnit]): Result.Result[List[String]] =
    for {
      a <- EnterSymbols.visitList(Analysis(), tul, EnterSymbols.transUnit)
      xmlFiles <- getXmlFiles(a, tul)
      cppFiles <- getCppFiles(tul)
    } 
    yield xmlFiles ++ cppFiles

  def getCppFiles(tul: List[Ast.TransUnit]) =
    for {
      s <- ComputeCppFiles.visitList(Map(), tul, ComputeCppFiles.transUnit)
    }
    yield s.toList.map(_._1)

  def getXmlFiles(a: Analysis, tul: List[Ast.TransUnit]) =
    for {
      s <- ComputeXmlFiles.visitList(
        XmlWriterState(a),
        tul,
        ComputeXmlFiles.transUnit
      )
    }
    yield s.locationMap.toList.map(_._1)

}
