import { getServerSession } from 'next-auth'
import { authOptions } from "../../app/api/auth/[...nextauth]/route"
import { Journal } from '../../models/journal';
import JournalCard from './journalCard';
import NewJournalForm from './newJournalForm';

const getJournals = async (id_token: string) => {
  const url = process.env.NEXT_PUBLIC_JOURNAL_APP_API + "/journals"

  const res = await fetch(
    url,
    {
      method: "GET",
      headers: {
        authorization: `Bearer ${id_token}`,
        "Content-Type": "application/json"
      },
    }
  )

  if (res.status == 200) {
    const result = await res.json();
    return result;
  }
  else if (res.status == 401)
    console.log("Error: Unauthorized user detected...");

  return [];
}


export default async function Journals() {
  const session = await getServerSession(authOptions);
  const journals = (session?.user) ? await getJournals(session.user.id_token) : [];

  return (
    <div className="journals-screen">
      <NewJournalForm />
        <div className="journal-list">
          {
            journals.map(
              (journal: Journal) => (
                <JournalCard key={journal.id} journal={journal} />
              )
            )
          }
        </div>
      </div>
  )


}