# alpha-cake
A Clojure core.logic implementation of the Google Code Jam
[Alphabet Cake problem](https://code.google.com/codejam/contest/5304486/dashboard)

Recently, my team at [work](https://www.etsy.com) went through this exercise from an imperative point of
view, trying to fulfill the requirements by generating one valid solution. This got me thinking about what
it would take to generate _all_ valid solutions, which in turn made me think of logic programming.

I've never done anything significant with logic programming, and I'm happy to report that the power of the
paradigm was not oversold, nor was its mind-bendingness.  In the end, my implementation only takes a little
over 100 lines (including boilerplate) to generate all solutions to any valid input to the problem. Not
bad!

I was also struck by how hard it was to think through a problem "backwards", but this really paid dividends
in the final result. The code ends up being laser-focused on its goals, which is obvious in retrospect, but
I'm left with a feeling how silly it is that most of the code I've written is so stubbornly silent about
what it's attempting to do. Normally, we cover this with comments and tests, but coding explicitly with
goals is a great thing to have in my toolbox.

Enjoy!

Just to give you a taste, here's the first example from the problem; there are six solutions.

```clj
boot.user> (alpha-cake "G??
                        ?C?
                        ??J")
GGG
CCJ
CCJ

GGG
CCC
JJJ

GGJ
CCJ
CCJ

GCJ
GCJ
GCJ

GCC
GCC
GJJ

GCC
GCC
JJJ

6
```
