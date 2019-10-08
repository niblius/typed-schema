/*
package ru.tinkoff.tschema.http4s

package ru.tinkoff.tschema.example.test

object l
{
  val macroInterface: ru.tinkoff.tschema.finagle.MkService.macroInterface.type = ru.tinkoff.tschema.finagle.MkService.macroInterface;
  import shapeless.{$colon$colon, HNil};
  macroInterface.serve[H, ru.tinkoff.tschema.typeDSL.Prefix[Symbol @@ String("filters")]].apply[shapeless.HNil, shapeless.HNil]((shapeless.HNil: shapeless.HNil))(finagle.this.Serve.prefix[H, Symbol @@ String("filters"), shapeless.HNil](FiltersModule.this.evidence$2, FiltersModule.this.evidence$1, common.this.Name.symbolName[Symbol @@ String("filters")](shapeless.Witness.mkWitness[Symbol with shapeless.tag.Tagged[String("filters")]](scala.Symbol.apply("filters").asInstanceOf[Symbol @@ String("filters")].asInstanceOf[Symbol with shapeless.tag.Tagged[String("filters")]]))), FiltersModule.this.evidence$1).apply(((input$macro$1: shapeless.HNil) => macroInterface.serve[H, ru.tinkoff.tschema.typeDSL.Tag[Symbol @@ String("filters")]].apply[shapeless.HNil, shapeless.HNil](input$macro$1)(finagle.this.Serve.serveMeta[H, ru.tinkoff.tschema.typeDSL.Tag[Symbol @@ String("filters")], shapeless.HNil], FiltersModule.this.evidence$1).apply(((input$macro$2: shapeless.HNil) => macroInterface.serve[H, ru.tinkoff.tschema.typeDSL.Prefix[Symbol @@ String("echo")]].apply[shapeless.HNil, shapeless.HNil](input$macro$2)(finagle.this.Serve.prefix[H, Symbol @@ String("echo"), shapeless.HNil](FiltersModule.this.evidence$2, FiltersModule.this.evidence$1, common.this.Name.symbolName[Symbol @@ String("echo")](shapeless.Witness.mkWitness[Symbol with shapeless.tag.Tagged[String("echo")]](scala.Symbol.apply("echo").asInstanceOf[Symbol @@ String("echo")].asInstanceOf[Symbol with shapeless.tag.Tagged[String("echo")]]))), FiltersModule.this.evidence$1).apply(((input$macro$3: shapeless.HNil) => macroInterface.serve[H, ru.tinkoff.tschema.typeDSL.Key[Symbol @@ String("echo")]].apply[shapeless.HNil, shapeless.HNil](input$macro$3)(finagle.this.Serve.serveKey[H, Symbol @@ String("echo"), shapeless.HNil], FiltersModule.this.evidence$1).apply(((input$macro$4: shapeless.HNil) => macroInterface.serve[H, ru.tinkoff.tschema.typeDSL.QueryParam[Symbol @@ String("filt"),ru.tinkoff.tschema.example.Filters]].apply[shapeless.HNil, shapeless.labelled.FieldType[Symbol @@ String("filt"),ru.tinkoff.tschema.example.Filters] :: shapeless.HNil](input$macro$4)(finagle.this.Serve.queryParamServe[H, Symbol @@ String("filt"), ru.tinkoff.tschema.example.Filters, shapeless.HNil](FiltersModule.this.evidence$2, FiltersModule.this.evidence$1, common.this.Name.symbolName[Symbol @@ String("filt")](shapeless.Witness.mkWitness[Symbol with shapeless.tag.Tagged[String("filt")]](scala.Symbol.apply("filt").asInstanceOf[Symbol @@ String("filt")].asInstanceOf[Symbol with shapeless.tag.Tagged[String("filt")]])), example.this.Filters.httpParam$macro$11), FiltersModule.this.evidence$1).apply(((input$macro$5: shapeless.labelled.FieldType[Symbol @@ String("filt"),ru.tinkoff.tschema.example.Filters] :: shapeless.HNil) => macroInterface.serve[H, ru.tinkoff.tschema.typeDSL.Get].apply[shapeless.labelled.FieldType[Symbol @@ String("filt"),ru.tinkoff.tschema.example.Filters] :: shapeless.HNil, shapeless.labelled.FieldType[Symbol @@ String("filt"),ru.tinkoff.tschema.example.Filters] :: shapeless.HNil](input$macro$5)(finagle.this.Serve.serveMethodGet[H, shapeless.labelled.FieldType[Symbol @@ String("filt"),ru.tinkoff.tschema.example.Filters] :: shapeless.HNil](FiltersModule.this.evidence$2, FiltersModule.this.evidence$1), FiltersModule.this.evidence$1).apply(((input$macro$6: shapeless.labelled.FieldType[Symbol @@ String("filt"),ru.tinkoff.tschema.example.Filters] :: shapeless.HNil) => (input$macro$6 match {
  case (head: shapeless.labelled.FieldType[Symbol @@ String("filt"),ru.tinkoff.tschema.example.Filters], tail: shapeless.HNil)shapeless.labelled.FieldType[Symbol @@ String("filt"),ru.tinkoff.tschema.example.Filters] :: shapeless.HNil((filt$macro$7 @ _), _) => {
def res: ru.tinkoff.tschema.example.Filters = ru.tinkoff.tschema.example.FiltersModule.handler.echo(filt$macro$7);
val route: macroInterface.RoutePA[ru.tinkoff.tschema.example.Filters] = macroInterface.route[ru.tinkoff.tschema.example.Filters](res);
route.apply[H, shapeless.labelled.FieldType[Symbol @@ String("filt"),ru.tinkoff.tschema.example.Filters] :: shapeless.HNil, ru.tinkoff.tschema.example.Filters](input$macro$6)(FiltersModule.this.evidence$2, FiltersModule.this.evidence$1, finagle.tethysInstances.tethysEncodeComplete[H, ru.tinkoff.tschema.example.Filters](FiltersModule.this.evidence$1, example.this.Filters.tethysWriter$macro$9, finagle.tethysInstances.tethysEncodeComplete$default$3[H, ru.tinkoff.tschema.example.Filters]))
}
}: H[com.twitter.finagle.http.Response])))))))))))))
}

object b {
  {
    val macroInterface = _root_.ru.tinkoff.tschema.finagle.MkService.macroInterface;
    import shapeless.{$colon$colon, HNil};
    macroInterface.serve[H, ru.tinkoff.tschema.typeDSL.Prefix[Symbol @@ String("filters")]]((HNil: HNil)).apply((
    (input$macro$1) => macroInterface.serve[H, ru.tinkoff.tschema.typeDSL.Tag[Symbol @@ String("filters")]](input$macro$1).apply((
    (input$macro$2) => macroInterface.serve[H, ru.tinkoff.tschema.typeDSL.Prefix[Symbol @@ String("echo")]](input$macro$2).apply((
    (input$macro$3) => macroInterface.serve[H, ru.tinkoff.tschema.typeDSL.Key[Symbol @@ String("echo")]](input$macro$3).apply((
    (input$macro$4) => macroInterface.serve[H, ru.tinkoff.tschema.typeDSL.QueryParam[Symbol @@ String("filt"),ru.tinkoff.tschema.example.Filters]](input$macro$4).apply((
    (input$macro$5) => macroInterface.serve[H, ru.tinkoff.tschema.typeDSL.Get](input$macro$5).apply((
      (input$macro$6) => macroInterface.makeResult[H, ru.tinkoff.tschema.example.Filters](input$macro$6)(ru.tinkoff.tschema.example.FiltersModule.handler)("echo")
      ))
    ))
    ))
    ))
    ))
    ))
  }
}

(prefixFilters: Filter).process(HNil, fInner1)

((in, f) => fr(f(in)))(HNil, fInner1) -> fr = Routed.checkPrefix(Name[name].string, _)

*/