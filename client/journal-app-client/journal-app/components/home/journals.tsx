import { getServerSession } from 'next-auth'
import { authOptions } from "../../app/api/auth/[...nextauth]/route"
import Image from 'next/image';

const getJournals = async (id_token : string)=>{
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

    if (res.status == 200){ 
      const result = await res.json();
      return result;
    }
    else if (res.status == 401)
      console.log("Error: Unauthorized user detected...");
    
    return [];
  }

  
export default async function Journals(){
    const session = await getServerSession(authOptions);
    const journals = (session?.user)? await getJournals(session.user.id_token) : [];
  
    console.log(journals);

    return (
        <div className="journals-screen">
            <h2>My Journals</h2>
            <div>
              <button className="create-journal-btn btn-link animated-entry-top"> New Journal </button>
            </div>
            <div className="journal-list">
              {
                journals.map(
                  (journal : any)=>(
                    <div key={journal.id}>
                      <Image src={journal.img}
                        alt={journal.name}
                        width={320}
                        height={480} />
                      <h1>{journal.name}</h1>
                    </div>
                  )
                )
              }
            </div>
        </div>
    )


}