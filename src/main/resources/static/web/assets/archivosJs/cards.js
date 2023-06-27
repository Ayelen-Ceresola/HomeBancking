let {createApp} = Vue

createApp({
    data(){
        return {
            clients:[],
            cards:[],
            debitCard:[],
            creditCard:[],
        }
    },

    created(){
        this.loadData()

    },


    methods:{
        loadData(){
            axios.get("http://localhost:8080/api/clients/current")
            .then((res) => {
                this.clients = res.data
                console.log(this.clients)
                
                this.cards= res.data.cards
                console.log(this.cards)
                this.debitCard= res.data.cards.filter(card=> card.type == "DEBIT").sort((a,b)=> a.id - b.id)
                console.log(this.debitCard)
                this.creditCard= res.data.cards.filter(card=> card.type == "CREDIT").sort((a,b)=> a.id - b.id)
                console.log(this.creditCard)




            })
            .catch(err => console.log (err))
        },
        selectColor(item){
            if(item.color == "SILVER"){
                return "silverColor"
            }
            else if(item.color == "GOLD"){
                return "goldColor"
            }
            else if(item.color == "TITANIUM"){
                return "titaniumColor"
            }

        },
        logout(){
            axios.post("/api/logout")
            .then((res) => {
                window.location.href= "/web/pages/index.html"
            })
            .catch(err => console.log(err))
        }
    }

}).mount ("#app")