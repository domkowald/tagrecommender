## Tag Recommender Framework

# Description
The aim of this work is to provide the community with a simple to use, generic tag-recommender framework to evaluate novel tag-recommender algorithms with a set of well-known std. IR metrics such as MAP, MRR, P@k, R@k, F1@k and folksonomy datasets such as BibSonomy, CiteULike, LastFM or Delicious and to benchmark the developed approaches against state-of-the-art tag-recommender algorithms such as MP, MP_r, MP_u, MP_u,r, CF, APR, FR, GIRP, GIRPTM, etc.

The software already contains two novel tag-recommender approaches based on cognitive science models. The first one ([3Layers](http://www.christophtrattner.info/pubs/cikm2013.pdf)) uses category information and is based on the ALCOVE theory. The second one ([BLL+C](http://arxiv.org/pdf/1312.5111.pdf)) uses time information is based on the ACT-R theory.

This software is free of charge for academic purposes. Please contact the author if you want to use this software for commercial purposes. You are not allowed to redistribute this software or its source code. 

Please cite [the papers](https://github.com/domkowald/tagrecommender/wiki#references) if you use this software in one of your publications.

# Download
The source-code can be directly checked-out through this repository. It contains an Eclipse project to edit and build it and an already deployed .jar file for direct execution. Furthermore, the folder structure that is provided in the repository is needed, where _csv_ is the input directory and _metrics_ is the output directory in the _data_ folder. Both of these directories contain subdirectories for the different datasets:
* bib_core for BibSonomy
* cul_core for CiteULike
* flickr_core for Flickr
* wiki_core for Wikipedia (based on bookmarks from Delicious)

# How-to-use
The _tagrecommender_ .jar uses three parameters:
First the algorithm:
* bll_c for BLL and BLL+C (based on ACT-R theory)
* 3layers for 3Layers (based on ALCOVE theory)
* lda for Latent Dirichlet Allocation
* cf for Collaborative Filtering
* fr for Adapted PageRank and FolkRank
* girptm for GIRP and GIRPTM
* mp for MostPopular tags
* mp_u_r for MostPopular tags by user and/or resource

, second the dataset(-directory):
* bib for BibSonomy
* cul for CiteULike
* flickr for Flickr

and third the filename (without file extension)

**Example:**
`java -jar tagrecommender.jar bll_c bib bib_sample`

# Input format
The input-files have to be placed in the corresponding subdirectory and are in csv-format (file extension: .txt) with 5 columns (quotation marks are mandatory):
* User
* Resource
* Timestamp in seconds
* List of tags
* List of categories (optional)

**Example:**
"0";"13830";"986470059";"deri,web2.0,tutorial,www,conference";""

There are three files needed:
* one file for training (with _train suffix)
* one file for testing (with _test suffix)
* one file that first contains the training-set and then the test-set (no suffix - is used for generating indices for the calculations)

**Example:**
bib_sample_train.txt, bib_sample_test.txt, bib_sample.txt (combination of train and test file)

# Output format
The output-file is generated in the corresponding subdirectory and is in csv-format with 5 columns:
* Recall
* Precision
* F1-score
* Mean Reciprocal Rank
* Mean Average Precision

for _k_ = 1 to 10 (each line is one _k_)

**Example:**
0,5212146123336273;0,16408544726301685;0,22663857529082376;0,26345775109372344;0,3242776089324113

# References
* Kowald, D., Seitinger, P., Trattner, C. and Ley, T.: [Long Time No See: The Probability of Reusing Tags as a Function of Frequency and Recency](http://arxiv.org/pdf/1312.5111.pdf) (under review).
* Seitinger, P., Kowald, D., Trattner, C. and Ley, T.: [Recommending Tags with a Model of Human Categorization](http://www.christophtrattner.info/pubs/cikm2013.pdf). In Proceedings of the ACM International Conference on Information and Knowledge Management (CIKM 2013), ACM, New York, NY, USA, 2013.
