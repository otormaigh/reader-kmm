import SwiftUI
import shared

struct ContentView: View {
    @ObservedObject private(set) var viewModel: ViewModel
    
    var body: some View {
        NavigationView {
            listView()
                .navigationBarTitle("HackerNews")
        }
    }
    
    private func listView() -> AnyView {
        switch viewModel.items {
        case .loading:
            return AnyView(Text("Loading...").multilineTextAlignment(.center))
        case .result(let listItems):
            return AnyView(
                List(listItems) { listItem in
                    StoryListItemView(listItem: listItem)
                }
                .padding(.leading, -20)
                .padding(.trailing, -20)
            )
        case .error(let description):
            return AnyView(Text(description).multilineTextAlignment(.center))
        }
    }
    
    enum UIState {
        case loading
        case result([HackerNewsItem])
        case error(String)
    }
    
    class ViewModel: ObservableObject {
        let itemStore: ItemStore
        
        @Published var items = UIState.loading
        
        init(itemStore: ItemStore) {
            self.itemStore = itemStore
            self.fetchItems()
        }
        
        func fetchItems() {
            self.items = UIState.loading
            itemStore.fetchAllItems { (items, error) in
                if let items = items {
                    self.items = UIState.result(items)
                } else {
                    self.items = .error(error?.localizedDescription ?? "error")
                }
            }
        }
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView(viewModel: .init(itemStore: ItemStore(databaseDriverFactory: DatabaseDriverFactory())))
    }
}


struct StoryListItemView: View {
    var listItem: HackerNewsItem
    
    var body: some View {
        HStack(alignment: .center) {
            Text(String(listItem.score))
                .font(.subheadline)
                .frame(minWidth: 32)
            VStack(alignment: .leading) {
                Text(listItem.title)
                    .lineLimit(2)
                    .font(.headline)
                Text(listItem.urlHost)
                    .font(.footnote)
            }
        }
        .frame(maxWidth: .infinity, alignment: .leading)
        .id(listItem.id)
    }
}


extension HackerNewsItem: Identifiable { }
