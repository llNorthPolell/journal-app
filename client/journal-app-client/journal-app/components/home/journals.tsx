import { getServerSession } from 'next-auth'
import { authOptions } from "../../app/api/auth/[...nextauth]/route"
import { Journal } from '../../models/journal';
import JournalCard from './journalCard';
import NewJournalForm from './newJournalForm';
import { getJournalsByAuthor } from '../../lib/journalAPI';



export default async function Journals() {
  const session = await getServerSession(authOptions);
  const journals = (session?.user) ? await getJournalsByAuthor(session.user.id_token) : [];

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