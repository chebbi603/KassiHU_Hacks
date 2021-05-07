import string
import re
import nltk

nltk.download('stopwords')
stop_words = set(stopwords.words('english'))

def count_word_frequencies(cleaned_data):
    word_counts = {}
    for doc in cleaned_data:
        words = doc.split()
        for word in words:
            if word not in stop_words:
                if word not in word_counts:
                    word_counts[word] = 1
                else:
                    word_counts[word] += 1
    return word_counts

def sort_words_by_frequency(word_counts):
    sorted_words = sorted(word_counts.items(), key=lambda x: x[1], reverse=True)
    top_words = [word[0] for word in sorted_words[:1000]]
    return top_words


def calculate_idf(cleaned_data, word):
    document_frequency = sum([1 for doc in cleaned_data if word in doc])
    inverse_document_frequency = len(cleaned_data) / (document_frequency)
    return inverse_document_frequency

def calculate_tfidf(cleaned_data, top_words):
    tfidf_table = []
    total_docs = len(cleaned_data)
    
    for doc in cleaned_data:
        words = doc.split()
        doc_vector = []
        
        for word in top_words:
            if word in words:
                tf = words.count(word) / len(words)
                idf = calculate_idf(cleaned_data, word)
                tfidf = tf * idf
                doc_vector.append(tfidf)
            else:
                doc_vector.append(0)
        
        tfidf_table.append(doc_vector)
    
    return tfidf_table