export function processDashboardWidgets(config, journalEntriesList) {
    if (config==null){
        console.log("Error: config is undefined.");
        return;
    } 

    let processedConfigs = [];

    function generateLastEntry(){
        let payload = {
            overview: "You have no journal entries at the moment. Please create one."
        };
        if (journalEntriesList==null || journalEntriesList.length===0) return payload;

        const lastEntry = journalEntriesList[journalEntriesList.length-1];
        payload = {
            overview: lastEntry.overview,
            key: lastEntry.key
        }
        return payload;
    }

    function generateLineGraph(widget){
        let payload = {
            title : widget.title,
            labels : widget.labels,
            data : {
                x: [],
                y: [...widget.data.yValues]
            }
        };
        payload.data.y[0].data=[];

        let dataset=[];

        journalEntriesList.forEach(
            journalEntry => {
                const journalBodyItem = journalEntry.journalBodyItems.find(
                    journalBodyItem=> journalBodyItem.topic === widget.data.yValues[0].topic
                );

                if (journalBodyItem == undefined) return;
                const record = journalBodyItem.recordList.find(
                    record=> record.key===widget.data.yValues[0].record
                );
                
                if (record == undefined) return;

                dataset.push({
                    x:journalEntry[widget.data.xValue],
                    y:record.value
                });
            }
        );
        
        dataset.sort((a,b)=>{return a.x>b.x});

        dataset.forEach(data=>{
            payload.data.x.push(data.x);
            payload.data.y[0].data.push(data.y);
        })

        return payload;
    }


    config.map(
        widget=> {
            if(widget.type==="last-entry")
                widget.payload = generateLastEntry();
            else if (widget.type==="line-graph"){
                widget.payload = generateLineGraph(widget);
            }

            if (widget.payload!=null)
                processedConfigs.push(widget);
        }
    )

    return processedConfigs;
}


