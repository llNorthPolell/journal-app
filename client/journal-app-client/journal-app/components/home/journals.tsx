import { getServerSession } from 'next-auth'
import { authOptions } from "../../app/api/auth/[...nextauth]/route"
import { Journal } from '../../models/journal';
import JournalCard from './journalCard';
import NewJournalForm from './newJournalForm';
import { getJournalsByAuthor } from '../../lib/journalAPI';



export default async function Journals() {
  const session = await getServerSession(authOptions);
  console.log(`components/journals - ${JSON.stringify(session)}`);
  const journals = (session?.user) ? await getJournalsByAuthor(session.user.id_token) : [];

  return (
    <div className="journals-screen">
      <NewJournalForm />
        <div className="journal-list">
          {
            (journals && journals.length > 0)?
              journals.map(
                (journal: Journal) => (
                  <JournalCard key={journal.id} journal={journal} />
                )
              )
              : 
              <p>Timed out while trying to fetch journals...</p>
          }
        </div>
      </div>
  )


}