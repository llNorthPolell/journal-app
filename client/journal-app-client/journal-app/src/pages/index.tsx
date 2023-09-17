import Head from 'next/head'
import Image from 'next/image'
import Link from 'next/link'


export default function Home() {
  return (
    <>
      <Head>
        <link
          rel="preload"
          href="https://firebasestorage.googleapis.com/v0/b/journal-app-75df1.appspot.com/o/Home%20Page%2FHero.png?alt=media&token=516f553f-722a-4d5a-86fc-68bfa29d37b9"
          as="image"
        />
      </Head>
      <main>
        <header className="hero">
          <div className="hero__text-box">
            <h1 className="heading-primary">
              <span className="heading-primary--main animated-entry-left">Keep track of your progress on the fly</span>
              <span className="heading-primary--sub animated-entry-right">Tired of using Excel to keep track of things? Want a visually-appealing UI to help you track your progress? You&apos;ve come to the right place!</span>
            </h1>
            <Link className="hero__btn btn-link btn-link-popout-hover animated-entry-left" href="/login">Try for Free</Link>
          </div>
        </header>

        <section className="about">
          <div className="about__text-box">
            <h2 className="heading-secondary animated-entry-left">What is &lt;Journal App&gt;?</h2>
            <p className="paragraph animated-entry-left">&lt;Journal App&gt; is a journalling app that provides you with a set of tools to help you stay on track and monitor your regular tasks.
              You may set goals that are automatically tracked each time you create a new entry, display your progress with charts using your own metrics, and more! Click the link below
              to learn more.
            </p>
            <Link className="about__link btn-link-outline animated-entry-left" href="#">Learn More &rarr; </Link>
          </div>
          <Image src={"https://firebasestorage.googleapis.com/v0/b/journal-app-75df1.appspot.com/o/Home%20Page%2FPlanner.png?alt=media&token=c55ca3b6-560a-4a3b-9310-f6996ffbcfd9"}
            alt="Picture of Planner"
            className="about__image"
            width={1170}
            height={780} />
        </section>

        <section className="use-cases">
          <h2 className="heading-secondary">Great for many routines and tasks, including </h2>
          <div className="use-cases__row">
            <div className="card card-animated-hover use-cases__card animated-entry-top delay-1">
              <Image
                src="https://firebasestorage.googleapis.com/v0/b/journal-app-75df1.appspot.com/o/Home%20Page%2FUkeleleBooks.png?alt=media&token=fe9f791c-6462-460d-933d-a6b8ba2886b5"
                alt=""
                className="card__image"
                width={370}
                height={227}
              />
              <div className="card__body">
                <h3 className="card__heading heading-tertiary">Great for Practice Logs!</h3>
                <p className="card__description paragraph">With graphs, goals and a scribbleboard, organize yourself to achieve perfection!</p>
              </div>
            </div>
            <div className="card card-animated-hover use-cases__card animated-entry-top delay-2">
              <Image
                src="https://firebasestorage.googleapis.com/v0/b/journal-app-75df1.appspot.com/o/Home%20Page%2FResearchPapers.png?alt=media&token=4f5c781a-be5e-4090-821e-226326145b99"
                alt=""
                className="card__image"
                width={370}
                height={227}
              />
              <div className="card__body">
                <h3 className="card__heading heading-tertiary">Track Personal Projects! </h3>
                <p className="card__description paragraph">With the activity board, counters and scrum board, keep track of your progress and plan at ease. </p>
              </div>
            </div>
            <div className="card card-animated-hover use-cases__card animated-entry-top delay-3">
              <Image
                src="https://firebasestorage.googleapis.com/v0/b/journal-app-75df1.appspot.com/o/Home%20Page%2FChemistry.png?alt=media&token=2bef70fb-6683-411f-b770-729c60200020"
                alt=""
                className="card__image"
                width={370}
                height={227}
              />
              <div className="card__body">
                <h3 className="card__heading heading-tertiary">For science!</h3>
                <p className="card__description paragraph">With graphs, counters and scribbleboard, store your research data in a presentable manner. </p>
              </div>
            </div>
          </div>
        </section>
      </main>
    </>
  )
}
