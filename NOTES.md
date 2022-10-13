# Monads

 Monads are a kind of types which have some fundamental ops.

```scala
    trait MonadTemplate[A] {
    def unit(value: A) : MonadTemplate[A] // also called pure or apply
    def flatMap[B](f : A => MonadTemplate[B]) : MonadTemplate[B] // also called bind
}
```

List, Option, Try, Future, stream, Set are all monads


Operations must satisfy the monad laws:

+ left-identity
  + unix(x).flatMap(f) == f(x)
+ right-identity
  + aMonadInstance.flatMap(unit) == aMonadInstance
+ associativity 
  + m.flatMap(f).flatMap(g) == m.flatMap(x => f(x).flatmap(g))


## Example: List

### Left identity:

```scala
    List(x).flatMap(f) = 
        f(x) ++ Nil.flatMap(f) =
            f(x)
    
```

### Right identity:

```scala
    list.flatMap(x => List(x)) = 
        list
    
```

### Associativity:

```scala
    [a,b,c].flatMap(f).flatMap(g) =
  (f(a) ++ f(b) ++ f(c)).flatMap(g) = 
    f(a).flatMap(g) += f(b).flatMap(g) ++ f(c).flatMap(g) =
      [a,b,c].flatMap(f(_).flatMap(g)) =
        [a,b,c].flatMap(x => f(x).flatMap(g))
```

## Example: Option

### Left identity:

```scala
    Option(x).flatMap(f) = f(x)
    Some(x).flatMap(f) = f(x)
```

### Right identity:

```scala
    opt.flatMap(x => Option(x)) = opt
    some(x).flatMap(x => Option(x)) = 
    Option(x) = 
    Some(x)
```

### Associativity:

```scala
    o.flatMap(f).flatMap(g) = o.flatMap(x => f(x)).flatMap(g)
    Some(x).flatMap(x).flatMap(g) = f(x).flatMap(g)
    Some(v).flatMap(x => f(x).flatMap(g)) = f(v).flatMap(g)
```

